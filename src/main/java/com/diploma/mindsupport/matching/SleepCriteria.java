package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.service.PsychologistPatientMatchService;

import java.util.List;

public class SleepCriteria extends SurveyMatchCriteria implements Criteria {
    public SleepCriteria(List<Option> clientOptions, PsychologistPatientMatchService matchService) {
        super(clientOptions, matchService);
    }
}
