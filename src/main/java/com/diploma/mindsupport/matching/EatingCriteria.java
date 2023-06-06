package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.service.PsychologistPatientMatchService;

import java.util.List;

public class EatingCriteria extends SurveyMatchCriteria implements Criteria {
    public EatingCriteria(List<Option> clientOptions, PsychologistPatientMatchService matchService) {
        super(clientOptions, matchService);
    }
}
