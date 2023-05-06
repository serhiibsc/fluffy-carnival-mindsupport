package com.diploma.mindsupport.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class UserInfoDto {
    private String firstName;
    private String lastName;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String createdAt;
    private String language;
    private String about;
    private String city;
    private String country;
    private String imageBase64Data;
}
