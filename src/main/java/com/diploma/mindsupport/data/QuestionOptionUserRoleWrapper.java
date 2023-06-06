package com.diploma.mindsupport.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuestionOptionUserRoleWrapper {
    private String matchingId;
    private String questionId;
    private String question;
    private Map<String, String> options;
    private String userRole;
}
