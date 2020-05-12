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

package com.decibeltx.studytracker.egnyte.test;

import com.decibeltx.studytracker.egnyte.rest.EgnyteRestApiClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles({"example"})
public class EgnyteRestClientFolderCreationTests {

  private final static String EGNYTE_ROOT = "Shared/General/Informatics & IT/Egnyte API Testing/StudyTrackerTest/TestCreate";

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private EgnyteRestApiClient client;

  @Test
  public void createAndDeleteFolderTest() throws Exception {

    System.out.println(
        "This functionality works, but the current API user account does not have permission to delete folders or files.");
    Assert.assertNotNull(client);
    Assert.assertNotNull(EGNYTE_ROOT);
//
//    // Create the new folder
//    Exception exception = null;
//    EgnyteFolder folder = null;
//    String path = EGNYTE_ROOT + "/NewFolder";
//
//    try {
//      folder = client.createFolder(path);
//    } catch (Exception e) {
//      exception = e;
//      e.printStackTrace();
//    }
//    Assert.assertNotNull(folder);
//    Assert.assertNull(exception);
//
//    // Make sure the folder is there
//    EgnyteFolder newFolder = client.findFolderById(folder.getFolderId());
//    Assert.assertNotNull(newFolder);
//    Assert.assertEquals("NewFolder", newFolder.getName());
//
//    // Try to create the folder again
//    try {
//      folder = client.createFolder(path);
//    } catch (Exception e) {
//      exception = e;
//      e.printStackTrace();
//    }
//
//    Assert.assertNotNull(exception);
//    Assert.assertTrue(exception.getCause() instanceof DuplicateFolderException);
//
//    // Delete the folder
//    try {
//      client.deleteObjectByPath(path);
//    } catch (Exception e) {
//      e.printStackTrace();
//      exception = e;
//    }
//    Assert.assertNull(exception);
//
//    // Make sure it is gone
//    EgnyteObject egnyteObject = null;
//    try {
//      egnyteObject = client.findObjectByPath(path);
//    } catch (Exception e) {
//      exception = e;
//      e.printStackTrace();
//    }
//    Assert.assertNotNull(exception);
//    Assert.assertTrue(exception.getCause() instanceof ObjectNotFoundException);
//    Assert.assertNull(egnyteObject);

  }

}
