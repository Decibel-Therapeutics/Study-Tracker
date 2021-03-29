package com.decibeltx.studytracker.test.aws;

import com.decibeltx.studytracker.config.ExampleDataConfiguration;
import com.decibeltx.studytracker.config.MongoDataSourceConfiguration;
import com.decibeltx.studytracker.config.MongoRepositoryConfiguration;
import com.decibeltx.studytracker.config.ServiceConfiguration;
import com.decibeltx.studytracker.config.StorageServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({
    MongoRepositoryConfiguration.class,
    ServiceConfiguration.class,
    MongoDataSourceConfiguration.class,
    ExampleDataConfiguration.class,
    StorageServiceConfiguration.class,
})
@PropertySource("classpath:test.properties")
public class TestConfiguration {

}
