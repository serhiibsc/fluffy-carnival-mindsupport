package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.service.PsychologistPatientMatchService;

import java.util.List;

public class PatientExhaustedCriteria extends SurveyMatchCriteria implements Criteria {
    public PatientExhaustedCriteria(List<Option> clientOptions,
                                    PsychologistPatientMatchService matchService) {
        super(clientOptions, matchService);
    }
}
