package com.decibeltx.studytracker.config;

import com.decibeltx.studytracker.search.elasticsearch.ElasticsearchSearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.decibeltx.studytracker.search.elasticsearch")
@ConditionalOnProperty(name = "search.mode", havingValue = "elasticsearch")
public class ElasticsearchConfiguration {

  @Autowired
  private Environment env;

  @Bean
  public RestHighLevelClient client() {
    ClientConfiguration configuration = ClientConfiguration.builder()
        .connectedTo(env.getRequiredProperty("elasticsearch.host"))
        .usingSsl()
        .build();
    return RestClients.create(configuration).rest();
  }

  @Bean
  public ElasticsearchSearchService elasticsearchSearchService(){
    return new ElasticsearchSearchService();
  }

}
