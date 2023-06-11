package com.diploma.mindsupport.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAnswerDto {
    private Long id;
    private String username;
    private OptionDto option;
}
