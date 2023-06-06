package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
