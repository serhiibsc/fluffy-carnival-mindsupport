package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.UserAnswerDto;
import com.diploma.mindsupport.model.UserAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface UserAnswerMapper {

    @Mapping(target = "username", expression = "java(userAnswer.getUser().getUsername())")
    UserAnswerDto toUserAnswerDto(UserAnswer userAnswer);

    List<UserAnswerDto> toUserAnswerDto(List<UserAnswer> userAnswers);
}
