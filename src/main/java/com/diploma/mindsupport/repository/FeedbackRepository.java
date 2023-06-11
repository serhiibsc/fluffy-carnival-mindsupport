package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Course;
import com.diploma.mindsupport.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByCourse(Course course);

}
