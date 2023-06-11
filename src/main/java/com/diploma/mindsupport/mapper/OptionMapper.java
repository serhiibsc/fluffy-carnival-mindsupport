package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.OptionDto;
import com.diploma.mindsupport.model.Option;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface OptionMapper {
    OptionDto toOptionDto(Option option);

    Option toOption(OptionDto optionDto);

    List<Option> toOption(List<OptionDto> optionDtoList);
}
