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

package com.decibeltx.studytracker.web.example;

import com.decibeltx.studytracker.core.example.ExampleDataGenerator;
import com.decibeltx.studytracker.ldap.LdapUserRepositoryPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("example")
public class ExampleDataRunner implements CommandLineRunner {

  @Autowired
  private ExampleDataGenerator exampleDataGenerator;

  @Autowired
  private LdapUserRepositoryPopulator userRepositoryPopulator;

  @Override
  public void run(String... args) throws Exception {
    exampleDataGenerator.populateDatabase();
    userRepositoryPopulator.updateUserRepository();
  }
}
