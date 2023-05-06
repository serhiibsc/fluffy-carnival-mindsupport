package com.diploma.mindsupport.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserInfoRequest {
    private UserInfoDto userInfoDto;
    private String username;
}
