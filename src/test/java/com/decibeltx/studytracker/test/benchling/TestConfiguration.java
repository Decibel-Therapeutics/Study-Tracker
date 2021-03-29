package com.decibeltx.studytracker.test.benchling;

import com.decibeltx.studytracker.config.EventsServiceConfiguration;
import com.decibeltx.studytracker.config.ExampleDataConfiguration;
import com.decibeltx.studytracker.config.MongoDataSourceConfiguration;
import com.decibeltx.studytracker.config.MongoRepositoryConfiguration;
import com.decibeltx.studytracker.config.ServiceConfiguration;
import com.decibeltx.studytracker.config.StorageServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    MongoRepositoryConfiguration.class,
    ServiceConfiguration.class,
    MongoDataSourceConfiguration.class,
    ExampleDataConfiguration.class,
    StorageServiceConfiguration.class,
    EventsServiceConfiguration.class
})
public class TestConfiguration {

}
