package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.UserAnswerDto;
import com.diploma.mindsupport.mapper.UserAnswerMapperImpl;
import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserAnswer;
import com.diploma.mindsupport.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;
    private final OptionService optionService;
    private final UserService userService;
    private final UserAnswerMapperImpl mapper;

    public List<UserAnswerDto> saveUserAnswers(List<Long> optionIds, String username) {
        List<Option> options = optionService.getOptionsByIds(optionIds);
        User user = userService.getUserByUsernameOrThrow(username);
        return mapper.toUserAnswerDto(getUserAnswers(options, user).stream().map(this::save).toList());
    }

    private List<UserAnswer> getUserAnswers(List<Option> options, User user) {
        return options.stream()
                .map(o ->
                        UserAnswer.builder()
                                .option(o)
                                .user(user)
                                .build())
                .toList();
    }

    private UserAnswer save(UserAnswer userAnswer) {
        return userAnswerRepository.save(userAnswer);
    }
}
