package com.decibeltx.studytracker.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assay_type_fields")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AssayTypeField extends CustomEntityField {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assay_type_id")
  private AssayType assayType;

  public AssayTypeField() {
    super();
  }

  public AssayTypeField(String displayName, String fieldName,
      CustomEntityFieldType type) {
    super();
    this.setDisplayName(displayName);
    this.setFieldName(fieldName);
    this.setType(type);
    this.setRequired(false);
    this.setActive(true);
  }

  public AssayTypeField(String displayName, String fieldName,
      CustomEntityFieldType type, boolean required) {
    this.setDisplayName(displayName);
    this.setFieldName(fieldName);
    this.setType(type);
    this.setRequired(required);
    this.setActive(true);
  }

  public AssayTypeField(String displayName, String fieldName,
      CustomEntityFieldType type, boolean required, String description) {
    this.setDisplayName(displayName);
    this.setFieldName(fieldName);
    this.setType(type);
    this.setRequired(required);
    this.setActive(true);
    this.setDescription(description);
  }

}
