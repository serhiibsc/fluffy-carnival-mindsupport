package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Gender;
import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserAnswer;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClientPreferenceCriteria implements Criteria {
    private final List<Option> clientOptions;

    public ClientPreferenceCriteria(List<Option> clientOptions) {
        this.clientOptions = clientOptions;
    }

    @Override
    public Collection<User> meetCriteria(Collection<User> psychologists) {
        return psychologists.stream()
                .filter(this::matchCriteria)
                .collect(Collectors.toSet());
    }

    private boolean matchCriteria(User psychologist) {
        return clientOptions.stream()
                .anyMatch(o -> matchToSpecificOption(o, psychologist));
    }

    private boolean matchToSpecificOption(Option option, User psychologist) {
        String optionText = option.getOptionText();

        if (optionText.equals("Male therapist") &&
                psychologist.getUserInfo().getGender() == Gender.MAN) {
            return true;
        }

        if (optionText.equals("Female therapist") &&
                psychologist.getUserInfo().getGender() == Gender.WOMAN) {
            return true;
        }

        if (optionText.equals("Older therapist(45+)") &&
                Period.between(
                                psychologist.getUserInfo().getDateOfBirth(),
                                LocalDate.now())
                        .getYears() >= 45) {
            return true;
        }

        // 114 - is the option id for "Yes, I identify myself as part of LGBTQ+ community."
        return optionText.equals("Therapist from the LGBTQ+ community") &&
                psychologist.getUserAnswers().stream()
                        .map(UserAnswer::getOption)
                        .anyMatch(o -> o.getId() == 114);
    }
}
