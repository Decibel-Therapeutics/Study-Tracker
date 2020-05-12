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

package com.decibeltx.studytracker.idbs.eln;

import com.decibeltx.studytracker.idbs.exception.IdbsExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnProperty(name = "notebook.mode", havingValue = "idbs")
public class IdbsElnServiceConfiguration {

  @Autowired
  private Environment env;

  @Bean
  public ObjectMapper idbsElnObjectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public RestTemplate idbsElnRestTemplate() {
    RestTemplate restTemplate = new RestTemplateBuilder()
        .errorHandler(new IdbsExceptionHandler(idbsElnObjectMapper()))
        .build();
    MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
    httpMessageConverter.setObjectMapper(idbsElnObjectMapper());
    restTemplate.getMessageConverters().add(0, httpMessageConverter);
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setOutputStreaming(false);
    restTemplate.setRequestFactory(requestFactory);
    return restTemplate;
  }

  @Bean
  public IdbsElnOptions elnOptions() throws Exception {
    IdbsElnOptions options = new IdbsElnOptions();

    // Authentication
    if (env.containsProperty("idbs.eln.api.token")) {
      Assert.notNull(env.getRequiredProperty("idbs.eln.api.token"),
          "API token must not be null. Eg. idbs.eln.api.token=xxx");
      options.setApiToken(env.getRequiredProperty("idbs.eln.api.token"));
    } else {
      Assert.notNull(env.getRequiredProperty("idbs.eln.api.username"),
          "API username must not be null. Eg. idbs.eln.api.username=xxx");
      Assert.notNull(env.getRequiredProperty("idbs.eln.api.password"),
          "API password must not be null. Eg. idbs.eln.api.password=xxx");
      options.setUsername(env.getRequiredProperty("idbs.eln.api.username"));
      options.setPassword(env.getRequiredProperty("idbs.eln.api.password"));
    }

    Assert.notNull(env.getProperty("idbs.eln.api.root-url"),
        "IDBS ELN API root URL is not set.");
    options.setRootUrl(new URL(env.getRequiredProperty("idbs.eln.api.root-url")));
    Assert.notNull(env.getProperty("idbs.eln.api.root-entity"),
        "IDBS ELN API root entity is not set.");
    options.setRootEntity(env.getRequiredProperty("idbs.eln.api.root-entity"));

    return options;
  }

  @Bean
  public IdbsRestElnClient idbsRestElnClient(IdbsElnOptions options) throws Exception {
    return new IdbsRestElnClient(
        idbsElnRestTemplate(),
        options.getRootUrl(),
        options.getApiToken()
    );
  }

  @Bean
  public IdbsNotebookService idbsNotebookService() {
    return new IdbsNotebookService();
  }

}
