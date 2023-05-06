package com.diploma.mindsupport.dto;

import com.diploma.mindsupport.model.Gender;
import com.diploma.mindsupport.model.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileInfoResponse {
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;

    private String language;
    private String about;
    private String city;
    private String country;

    private ImageDto image;

    private String username;
    private String email;
    private UserRole userRole;
}
