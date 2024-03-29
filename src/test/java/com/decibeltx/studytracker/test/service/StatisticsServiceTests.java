package com.decibeltx.studytracker.test.service;

import com.decibeltx.studytracker.Application;
import com.decibeltx.studytracker.example.ExampleDataGenerator;
import com.decibeltx.studytracker.mapstruct.dto.SummaryStatisticsDto;
import com.decibeltx.studytracker.service.StatisticsService;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "example"})
public class StatisticsServiceTests {

  private static final long STUDY_COUNT = 6;

  private static final long ASSAY_COUNT = 2;

  private static final long PROGRAM_COUNT = 5;

  private static final long USER_COUNT = 3;

  private static final long ACTIVITY_COUNT = 13;

  @Autowired
  private StatisticsService statisticsService;

  @Autowired
  private ExampleDataGenerator exampleDataGenerator;

  @Before
  public void doBefore() {
    exampleDataGenerator.populateDatabase();
  }

  @Test
  public void currentTest() throws Exception {
    SummaryStatisticsDto summaryStatisticsDto = statisticsService.getCurrent();
    Assert.assertNotNull(summaryStatisticsDto);
    System.out.println(summaryStatisticsDto.toString());
    Assert.assertEquals(STUDY_COUNT, summaryStatisticsDto.getStudyCount());
    Assert.assertEquals(ASSAY_COUNT, summaryStatisticsDto.getAssayCount());
    Assert.assertEquals(PROGRAM_COUNT, summaryStatisticsDto.getProgramCount());
    Assert.assertEquals(USER_COUNT, summaryStatisticsDto.getUserCount());
    Assert.assertEquals(ACTIVITY_COUNT, summaryStatisticsDto.getActivityCount());
  }

  @Test
  public void lastMonthTest() throws Exception {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, -1);
    Date monthAgo = calendar.getTime();

    SummaryStatisticsDto summaryStatisticsDto = statisticsService.getAfterDate(monthAgo);
    Assert.assertNotNull(summaryStatisticsDto);
    System.out.println(summaryStatisticsDto.toString());

    Assert.assertEquals(STUDY_COUNT, summaryStatisticsDto.getStudyCount());
    Assert.assertEquals(ASSAY_COUNT, summaryStatisticsDto.getAssayCount());
    Assert.assertEquals(PROGRAM_COUNT, summaryStatisticsDto.getProgramCount());
    Assert.assertEquals(USER_COUNT, summaryStatisticsDto.getUserCount());
    Assert.assertEquals(ACTIVITY_COUNT, summaryStatisticsDto.getActivityCount());

  }

}
