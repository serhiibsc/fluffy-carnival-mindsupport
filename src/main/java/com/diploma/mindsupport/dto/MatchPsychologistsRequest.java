package com.diploma.mindsupport.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchPsychologistsRequest {
    List<OptionDto> optionDtoList;
}
