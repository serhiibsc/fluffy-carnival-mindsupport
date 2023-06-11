package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByOptionsEquals(List<Option> options);

}
