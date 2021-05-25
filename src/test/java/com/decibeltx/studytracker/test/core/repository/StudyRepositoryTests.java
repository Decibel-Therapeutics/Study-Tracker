package com.decibeltx.studytracker.test.core.repository;

import com.decibeltx.studytracker.Application;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.model.Comment;
import com.decibeltx.studytracker.model.FileStoreFolder;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.repository.ActivityRepository;
import com.decibeltx.studytracker.repository.CommentRepository;
import com.decibeltx.studytracker.repository.ELNFolderRepository;
import com.decibeltx.studytracker.repository.FileStoreFolderRepository;
import com.decibeltx.studytracker.repository.ProgramRepository;
import com.decibeltx.studytracker.repository.StudyRepository;
import com.decibeltx.studytracker.repository.UserRepository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class StudyRepositoryTests {

  @Autowired private UserRepository userRepository;
  @Autowired private ProgramRepository programRepository;
  @Autowired private ELNFolderRepository elnFolderRepository;
  @Autowired private FileStoreFolderRepository fileStoreFolderRepository;
  @Autowired private StudyRepository studyRepository;
  @Autowired private EntityManagerFactory entityManagerFactory;
  @Autowired private CommentRepository commentRepository;
  @Autowired private ActivityRepository activityRepository;

  @After
  public void doBefore() {
    activityRepository.deleteAll();
    commentRepository.deleteAll();
    studyRepository.deleteAll();
    programRepository.deleteAll();
    fileStoreFolderRepository.deleteAll();
    elnFolderRepository.deleteAll();
    userRepository.deleteAll();
  }

  private void createUser() {
    User user = new User();
    user.setAdmin(false);
    user.setUsername("test");
    user.setEmail("test@email.com");
    user.setDisplayName("Joe Person");
    user.setActive(true);
    user.setPassword("password");
    user.setAttributes(Collections.singletonMap("key", "value"));
    user.setTitle("Director");
    userRepository.save(user);
  }

  private void createProgram() {

    User user = userRepository.findByUsername("test").orElseThrow(RecordNotFoundException::new);

    Program program = new Program();
    program.setActive(true);
    program.setCode("TST");
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setName("Test Program");
    program.setAttributes(Collections.singletonMap("key", "value"));

    FileStoreFolder folder = new FileStoreFolder();
    folder.setPath("/path/to/test");
    folder.setName("test");
    folder.setUrl("http://test");
    program.setStorageFolder(folder);

    programRepository.save(program);

  }

  @Test
  public void newStudyTest() {

    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();

    try {

      createUser();
      createProgram();

      User user = userRepository.findByUsername("test").orElseThrow(RecordNotFoundException::new);
      Program program = programRepository.findByName("Test Program")
          .orElseThrow(RecordNotFoundException::new);

      Study study = new Study();
      study.setName("Test Study");
      study.setProgram(program);
      study.setCode(program.getCode() + "-10001");
      study.setDescription("This is a test");
      study.setCreatedBy(user);
      study.setLastModifiedBy(user);
      study.setOwner(user);
      study.setUsers(Collections.singleton(user));
      study.setStatus(Status.ACTIVE);
      study.setStartDate(new Date());
      study.setAttributes(Collections.singletonMap("key", "value"));
      studyRepository.save(study);
      Assert.assertNotNull(study.getId());

      List<Study> studies = studyRepository.findAll();
      Assert.assertNotNull(studies);
      Assert.assertFalse(studies.isEmpty());

      Study created = studies.get(0);
      Assert.assertEquals("Test Study", created.getName());
      Assert.assertTrue(created.getCode().endsWith("-10001"));
      Assert.assertNotNull(created.getCreatedAt());
      User owner = created.getOwner();
      Assert.assertNotNull(owner);
      Assert.assertEquals("test", owner.getUsername());

      Set<User> users = created.getUsers();
      Assert.assertFalse(users.isEmpty());
      User studyUser = users.stream().findFirst().orElseThrow(RecordNotFoundException::new);
      Assert.assertEquals("test", studyUser.getUsername());

      Comment comment = new Comment();
      comment.setText("This is a test");
      comment.setCreatedBy(user);
      comment.setStudy(created);
      commentRepository.save(comment);

      Study updated = studyRepository.findAll().get(0);
      Assert.assertFalse(updated.getComments().isEmpty());

      session.getTransaction().commit();

    } catch (Exception e) {
      e.printStackTrace();
      session.getTransaction().rollback();
    } finally {
      session.close();
    }

  }

  @Test
  public void updateStudyTest() {

    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();

    try {

      createUser();
      createProgram();
      newStudyTest();

      Study study = studyRepository.findAll().get(0);
      Assert.assertFalse(study.getUsers().isEmpty());
      User user = study.getUsers().stream().findFirst().get();
      Assert.assertNotNull(user);
      study.getUsers().remove(user);
      studyRepository.save(study);

      Study updated = studyRepository.findAll().get(0);
      Assert.assertTrue(updated.getUsers().isEmpty());
      Assert.assertEquals(1, userRepository.count());

      session.getTransaction().commit();

    } catch (Exception e) {
      e.printStackTrace();
      session.getTransaction().rollback();
    } finally {
      session.close();
    }

  }


}
