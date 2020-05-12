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

package com.decibeltx.studytracker.core.service.impl;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.repository.UserRepository;
import com.decibeltx.studytracker.core.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findByAccountName(String accountName) {
    return userRepository.findByAccountName(accountName);
  }

  @Override
  public List<User> search(String keyword) {
    return userRepository.findByDisplayNameLike(keyword);
  }

  @Override
  public void create(User user) {
    userRepository.insert(user);
  }

  @Override
  public void update(User user) {
    userRepository.findById(user.getId()).orElseThrow(RecordNotFoundException::new);
    userRepository.save(user);
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

}
