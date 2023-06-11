package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
