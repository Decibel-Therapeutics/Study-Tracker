package com.decibeltx.studytracker.repository;

import com.decibeltx.studytracker.model.AssayType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssayTypeRepository extends JpaRepository<AssayType, Long> {

  Optional<AssayType> findByName(String name);

}
