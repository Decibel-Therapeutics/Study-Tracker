package com.decibeltx.studytracker.benchling.test;

import com.decibeltx.studytracker.benchling.eln.BenchlingElnRestClient;
import com.decibeltx.studytracker.benchling.eln.entities.BenchlingProject;
import java.util.List;
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
public class BenchlingElnRestClientTests {

  @Autowired
  private BenchlingElnRestClient client;

  @Test
  public void findProjectsTest() {
    List<BenchlingProject> projects = client.findProjects();
    Assert.assertNotNull(projects);
    Assert.assertTrue(!projects.isEmpty());
    for (BenchlingProject project : projects) {
      System.out.println(project.toString());
    }
  }


}
