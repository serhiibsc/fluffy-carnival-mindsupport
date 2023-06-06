package com.diploma.mindsupport.service;

import com.diploma.mindsupport.model.Question;
import com.diploma.mindsupport.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public void saveQuestionList(List<Question> questionList) {
        questionRepository.saveAll(questionList);
    }
}
