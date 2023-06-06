package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<UserAnswer, Long> {
}
