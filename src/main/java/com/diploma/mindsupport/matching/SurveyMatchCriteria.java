package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserAnswer;
import com.diploma.mindsupport.service.PsychologistPatientMatchService;

import java.util.Collection;
import java.util.List;

public class SurveyMatchCriteria implements Criteria {
    /* Converted client options to psychologists' ones */
    private final List<Option> optionsConverted;

    public SurveyMatchCriteria(
            List<Option> clientOptions,
            PsychologistPatientMatchService matchService) {
        optionsConverted = matchService.getPsychologistOptionsByPatientOptions(clientOptions);
    }

    @Override
    public Collection<User> meetCriteria(Collection<User> psychologists) {
        return psychologists.stream()
                .filter(p -> matchToClientOptions(p.getUserAnswers()))
                .toList();
    }

    private boolean matchToClientOptions(List<UserAnswer> userAnswers) {
        return userAnswers.stream()
                .map(UserAnswer::getOption)
                .anyMatch(optionsConverted::contains);
    }
}
