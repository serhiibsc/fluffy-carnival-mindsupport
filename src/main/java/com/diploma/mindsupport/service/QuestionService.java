package com.diploma.mindsupport.service;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.Question;
import com.diploma.mindsupport.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MatchingService matchingService;

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public void saveQuestionList(List<Question> questionList) {
        questionRepository.saveAll(questionList);
    }

    public Question findByOptionId(Option option) {
        return questionRepository.findByOptionsEquals(List.of(option)).orElseThrow();
    }
}
