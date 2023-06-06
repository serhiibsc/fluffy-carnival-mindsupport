package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.QuestionDto;
import com.diploma.mindsupport.model.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question toQuestion(QuestionDto questionDto);

    List<Question> toQuestion(List<QuestionDto> questionDtoList);
}
