/*
 * Copyright 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.decibeltx.studytracker.web.config;

import com.decibeltx.studytracker.core.config.UserServiceAuditor;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.web.security.CombinedUserDetailsService;
import com.decibeltx.studytracker.web.security.DatabaseAuthProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfiguration {

  @Autowired
  private CombinedUserDetailsService combinedUserDetailsService;

  @Bean
  public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
    return new UserAuthenticationSuccessHandler();
  }

  @Bean
  public AuditorAware<User> auditorAware() {
    return new UserServiceAuditor();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DatabaseAuthProvider databaseAuthProvider() {
    return new DatabaseAuthProvider(combinedUserDetailsService, passwordEncoder());
  }

  @Configuration
  @Order(1)
  public static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
      BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
      entryPoint.setRealmName("api realm");
      return entryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/api/**")
          .authorizeRequests()
          .antMatchers(HttpMethod.POST).fullyAuthenticated()
          .antMatchers(HttpMethod.PUT).fullyAuthenticated()
          .antMatchers(HttpMethod.DELETE).fullyAuthenticated()
          .anyRequest().permitAll()
          .and()
          .httpBasic()
          .authenticationEntryPoint(apiAuthenticationEntryPoint())
          .and()
          .cors()
          .and()
          .exceptionHandling()
          .and()
          .headers()
          .frameOptions().disable()
          .httpStrictTransportSecurity().disable()
          .and()
          .csrf().disable();
    }

  }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Configuration
  @Order(3)
  public static class WebAppSecurityConfiguration extends WebSecurityConfigurerAdapter
      implements DisposableBean {

    @Value("${saml.sp}")
    private String samlAudience;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SAMLEntryPoint samlEntryPoint;

    @Autowired
    private SAMLAuthenticationProvider samlAuthenticationProvider;

    @Autowired
    private DatabaseAuthProvider databaseAuthProvider;

    @Autowired
    private SAMLLogoutFilter samlLogoutFilter;

    @Autowired
    private MetadataDisplayFilter metadataDisplayFilter;

    @Autowired
    private SAMLLogoutProcessingFilter samlLogoutProcessingFilter;

    @Autowired
    private SAMLDiscovery samlDiscovery;

    @Autowired
    private KeyManager keyManager;

    @Autowired
    private ExtendedMetadata extendedMetadata;

    @Autowired
    @Qualifier("saml")
    private SavedRequestAwareAuthenticationSuccessHandler samlAuthSuccessHandler;

    @Autowired
    @Qualifier("saml")
    private SimpleUrlAuthenticationFailureHandler samlAuthFailureHandler;

    @Autowired
    @Qualifier("saml")
    private Timer samlBackgroundTaskTimer;

    @Autowired
    @Qualifier("saml")
    private MultiThreadedHttpConnectionManager samlMultiThreadedHttpConnectionManager;

    public MetadataGenerator metadataGenerator() {
      MetadataGenerator metadataGenerator = new MetadataGenerator();
      metadataGenerator.setEntityId(samlAudience);
      metadataGenerator.setExtendedMetadata(extendedMetadata);
      metadataGenerator.setIncludeDiscoveryExtension(false);
      metadataGenerator.setKeyManager(keyManager);
      return metadataGenerator;
    }

    @Bean
    public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
      SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
      samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(samlAuthSuccessHandler);
      samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
      samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(samlAuthFailureHandler);
      return samlWebSSOHoKProcessingFilter;
    }

    // Processing filter for WebSSO profile messages
    @Bean
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
      SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
      samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
      samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(samlAuthSuccessHandler);
      samlWebSSOProcessingFilter.setAuthenticationFailureHandler(samlAuthFailureHandler);
      return samlWebSSOProcessingFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }

    @Bean
    public FilterChainProxy samlFilter() throws Exception {
      List<SecurityFilterChain> chains = new ArrayList<>();
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"),
          samlEntryPoint));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"),
          samlLogoutFilter));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"),
          metadataDisplayFilter));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"),
          samlWebSSOProcessingFilter()));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"),
          samlWebSSOHoKProcessingFilter()));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"),
          samlLogoutProcessingFilter));
      chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/discovery/**"),
          samlDiscovery));
      return new FilterChainProxy(chains);
    }

    @Bean
    public MetadataGeneratorFilter metadataGeneratorFilter() {
      return new MetadataGeneratorFilter(metadataGenerator());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .authenticationProvider(databaseAuthProvider)
          .authenticationProvider(samlAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http.csrf().disable();

      http.httpBasic()
          .authenticationEntryPoint((request, response, exception) -> {
            if (request.getRequestURI().endsWith("dosaml")) {
              samlEntryPoint.commence(request, response, exception);
            } else {
              return;
            }
          });

      http.addFilterBefore(metadataGeneratorFilter(), ChannelProcessingFilter.class)
          .addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
          .addFilterBefore(samlFilter(), CsrfFilter.class);

      http
          .authorizeRequests()
//            .antMatchers("/studies/new", "/study/*/assays/new", "study/*/edit",
//              "study/*/assays/*/edit", "/programs/new", "/users/new", "/admin",
//              "/assaytypes/new", "/assaytypes/*/edit")
//              .fullyAuthenticated()
          .antMatchers("/").permitAll()
          .antMatchers("/study/**", "/studies").permitAll()
          .antMatchers("/assays", "/assay/**").permitAll()
          .antMatchers("/assaytypes", "/assaytype/**").permitAll()
          .antMatchers("/saml/**").permitAll()
          .antMatchers("/static/**").permitAll()
          .antMatchers("/login/**", "/auth/**").permitAll()
          .anyRequest().fullyAuthenticated();

      http.logout()
          .logoutUrl("/logout")
          .logoutSuccessUrl("/")
          .invalidateHttpSession(true);

    }

    @Override
    public void destroy() throws Exception {
      this.samlBackgroundTaskTimer.purge();
      this.samlBackgroundTaskTimer.cancel();
      this.samlMultiThreadedHttpConnectionManager.shutdown();
    }

  }

}
