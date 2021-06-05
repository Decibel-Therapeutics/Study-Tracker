package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.controller.UserAuthenticationUtils;
import com.decibeltx.studytracker.events.EventsService;
import com.decibeltx.studytracker.events.util.EntryTemplateActivityUtils;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.mapstruct.dto.NotebookEntryTemplateDetailsDto;
import com.decibeltx.studytracker.mapstruct.dto.NotebookEntryTemplateSlimDto;
import com.decibeltx.studytracker.mapstruct.mapper.NotebookEntryTemplateMapper;
import com.decibeltx.studytracker.model.Activity;
import com.decibeltx.studytracker.model.NotebookEntryTemplate;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.service.ActivityService;
import com.decibeltx.studytracker.service.NotebookEntryTemplateService;
import com.decibeltx.studytracker.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/api/entryTemplate")
public class NotebookEntryTemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotebookEntryTemplateController.class);

    @Autowired
    private NotebookEntryTemplateService entryTemplateService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private NotebookEntryTemplateMapper mapper;

    private User getAuthenticatedUser() throws RecordNotFoundException {
        String username = UserAuthenticationUtils
                .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        return userService.findByUsername(username)
                .orElseThrow(RecordNotFoundException::new);
    }

    private NotebookEntryTemplate getTemplateById(Long id) throws RecordNotFoundException {
        return entryTemplateService
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Template not found: " + id));
    }

    @GetMapping("")
    public List<NotebookEntryTemplateSlimDto> getEntryTemplates() {
        LOGGER.info("Getting all entry templates");
        return mapper.toSlimList(entryTemplateService.findAll());
    }

    @GetMapping("/active")
    public List<NotebookEntryTemplateSlimDto> getActiveTemplates() {
        LOGGER.info("Getting all active entry templates");

        return mapper.toSlimList(entryTemplateService.findAllActive());
    }

    @PostMapping("")
    public HttpEntity<NotebookEntryTemplateDetailsDto> createTemplate(
        @RequestBody NotebookEntryTemplateDetailsDto dto) throws RecordNotFoundException {
        LOGGER.info("Creating new entry template : " + dto.toString());

        NotebookEntryTemplate notebookEntryTemplate = mapper.fromDetails(dto);
        authenticateUserAndApplyTemplateOperation(Optional.empty(), notebookEntryTemplate,
                entryTemplateService::create, EntryTemplateActivityUtils::fromNewEntryTemplate);

        return new ResponseEntity<>(mapper.toDetails(notebookEntryTemplate), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/status")
    public HttpEntity<?> updateTemplateStatus(@PathVariable("id") Long id,
                                                                  @RequestParam("active") boolean active)
            throws RecordNotFoundException {
        LOGGER.info("Updating template with id: " + id);

        NotebookEntryTemplate notebookEntryTemplate = getTemplateById(id);
        authenticateUserAndApplyTemplateOperation(Optional.of(active), notebookEntryTemplate,
                entryTemplateService::update,EntryTemplateActivityUtils::fromUpdatedEntryTemplate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public HttpEntity<NotebookEntryTemplateDetailsDto> updateEntryTemplate(
        @RequestBody NotebookEntryTemplateDetailsDto dto) {
        NotebookEntryTemplate notebookEntryTemplate = mapper.fromDetails(dto);
        getTemplateById(notebookEntryTemplate.getId());
        authenticateUserAndApplyTemplateOperation(Optional.empty(), notebookEntryTemplate,
                entryTemplateService::update, EntryTemplateActivityUtils::fromUpdatedEntryTemplate);
        return new ResponseEntity<>(mapper.toDetails(notebookEntryTemplate), HttpStatus.CREATED);
    }

    private void authenticateUserAndApplyTemplateOperation(Optional<Boolean> status, NotebookEntryTemplate notebookEntryTemplate,
                                                           Consumer<NotebookEntryTemplate> templateConsumer,
                                                           BiFunction<NotebookEntryTemplate, User, Activity> activityGenerator) {
        User user = getAuthenticatedUser();
        notebookEntryTemplate.setLastModifiedBy(user);
        status.ifPresent(notebookEntryTemplate::setActive);
        templateConsumer.accept(notebookEntryTemplate);
        publishEvents(notebookEntryTemplate, user, activityGenerator);
    }

    private void publishEvents(NotebookEntryTemplate notebookEntryTemplate, User user,
                               BiFunction<NotebookEntryTemplate, User, Activity> generateActivity) {
        Activity activity = generateActivity.apply(notebookEntryTemplate, user);
        activityService.create(activity);
        eventsService.dispatchEvent(activity);
    }
}
