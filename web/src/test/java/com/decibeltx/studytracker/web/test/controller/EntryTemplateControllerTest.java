package com.decibeltx.studytracker.web.test.controller;

import com.decibeltx.studytracker.core.example.ExampleDataGenerator;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.repository.UserRepository;
import com.decibeltx.studytracker.web.test.TestApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test", "example"})
public class EntryTemplateControllerTest {

    private static final int ENTRY_TEMPLATE_COUNT = 2;

    @Autowired
    private ExampleDataGenerator exampleDataGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void doBefore() {
        exampleDataGenerator.populateDatabase();
    }

    @Test
    public void allEntryTemplateTest() throws Exception {
        mockMvc.perform(get("/api/entryTemplate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(ENTRY_TEMPLATE_COUNT)))
                .andExpect(jsonPath("$[0]", hasKey("id")))
                .andExpect(jsonPath("$[0]", hasKey("name")))
                .andExpect(jsonPath("$[0]", hasKey("templateId")));
    }

    @Test
    public void createEntryTemplateTest() throws Exception {
        User user = userRepository.findByUsername("jsmith")
                .orElseThrow(RecordNotFoundException::new);
        EntryTemplate entryTemplate = new EntryTemplate();
        entryTemplate.setTemplateId("id3");
        entryTemplate.setName("table3");
        entryTemplate.setCreatedBy(user);
        entryTemplate.setLastModifiedBy(user);
        entryTemplate.setCreatedAt(new Date());
        entryTemplate.setUpdatedAt(new Date());
        mockMvc.perform(post("/api/entryTemplate")
                .with(user(user.getUsername()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(entryTemplate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasKey("id")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$", hasKey("name")))
                .andExpect(jsonPath("$.name", is("table3")))
                .andExpect(jsonPath("$", hasKey("templateId")))
                .andExpect(jsonPath("$.templateId", is("id3")));
    }

}
