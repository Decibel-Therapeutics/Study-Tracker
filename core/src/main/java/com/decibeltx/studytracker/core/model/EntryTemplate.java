package com.decibeltx.studytracker.core.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "templates")
@Data
public class EntryTemplate implements Persistable<String> {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull(message = "Template name must not be empty")
    private String name;

    @Indexed(unique = true)
    @NotNull(message = "Template id must not be empty")
    private String templateId;

    @CreatedBy
    @Linked(model = EntryTemplate.class)
    @NotNull
    @DBRef
    private User createdBy;

    @LastModifiedBy
    @Linked(model = EntryTemplate.class)
    @NotNull
    @DBRef
    private User lastModifiedBy;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private boolean active = true;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
