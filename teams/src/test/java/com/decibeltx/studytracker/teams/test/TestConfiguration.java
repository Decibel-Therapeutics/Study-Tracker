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

package com.decibeltx.studytracker.teams.test;

import com.decibeltx.studytracker.core.config.EventConfiguration;
import com.decibeltx.studytracker.core.config.ExampleDataConfiguration;
import com.decibeltx.studytracker.core.config.LocalStudyStorageServiceConfiguration;
import com.decibeltx.studytracker.core.config.MongoDataSourceConfiguration;
import com.decibeltx.studytracker.core.config.MongoRepositoryConfiguration;
import com.decibeltx.studytracker.core.config.ServiceConfiguration;
import com.decibeltx.studytracker.teams.TeamsServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({
    MongoDataSourceConfiguration.class,
    MongoRepositoryConfiguration.class,
    ServiceConfiguration.class,
    EventConfiguration.class,
    TeamsServiceConfiguration.class,
    ExampleDataConfiguration.class,
    LocalStudyStorageServiceConfiguration.class
})
@PropertySource("classpath:test.properties")
public class TestConfiguration {

}
