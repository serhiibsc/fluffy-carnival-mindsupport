package com.diploma.mindsupport.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswersRequest {
    List<Long> optionIds;
}
