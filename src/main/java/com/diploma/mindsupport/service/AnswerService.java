package com.diploma.mindsupport.service;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.Question;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserAnswer;
import com.diploma.mindsupport.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void saveUserAnswers(User user, Map<Question, Option> answers) {
        List<UserAnswer> answerList = answers.entrySet().stream()
                .map(entry -> UserAnswer.builder()
                            .user(user)
                            .option(entry.getValue())
                            .build())
                .toList();

        answerRepository.saveAll(answerList);
    }
}