package com.decibeltx.studytracker.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "notebook_entry_templates")
@Data
@EntityListeners(AuditingEntityListener.class)
public class NotebookEntryTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotNull(message = "Template name must not be empty")
    private String name;

    @Column(name = "template_id", nullable = false, unique = true)
    @NotNull(message = "Template id must not be empty")
    private String templateId;

    @CreatedBy
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @LastModifiedBy
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", nullable = false)
    private User lastModifiedBy;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public static NotebookEntryTemplate of(User user, String templateId,
                                           String name, Date timeStamp) {
        NotebookEntryTemplate notebookEntryTemplate = new NotebookEntryTemplate();
        notebookEntryTemplate.setTemplateId(templateId);
        notebookEntryTemplate.setName(name);
        notebookEntryTemplate.setCreatedBy(user);
        notebookEntryTemplate.setLastModifiedBy(user);
        notebookEntryTemplate.setCreatedAt(timeStamp);
        notebookEntryTemplate.setUpdatedAt(timeStamp);
        return notebookEntryTemplate;
    }

}
