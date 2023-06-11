package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.MatchPsychologistsRequest;
import com.diploma.mindsupport.dto.OptionDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.mapper.AvailabilityMapperImpl;
import com.diploma.mindsupport.mapper.UserInfoMapperImpl;
import com.diploma.mindsupport.matching.*;
import com.diploma.mindsupport.model.Availability;
import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.UserAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {
    private final UserService userService;
    private final PsychologistPatientMatchService matchService;
    private final AvailabilityService availabilityService;
    private final UserInfoMapperImpl userInfoMapper;
    private final AvailabilityMapperImpl availabilityMapper;

    public List<UserProfileInfoResponse> matchPsychologists(MatchPsychologistsRequest request, String username) {
        Availability availability = availabilityMapper.toAvailability(request.getAvailability());
        return AndCriteria.builder()
                .criteriaList(createCriteriaList(availability, getUserOptions(username)))
                .build()
                .meetCriteria(userService.getPsychologists()).stream()
                .toList().stream()
                .map(userInfoMapper::userToUserProfileInfo)
                .toList();
    }

    private List<Option> getUserOptions(String username) {
        return userService.getUserByUsernameOrThrow(username)
                .getUserAnswers().stream()
                .map(UserAnswer::getOption)
                .toList();
    }

    private List<Criteria> createCriteriaList(Availability availability, List<Option> options) {
        ApathyCriteria apathyCriteria = new ApathyCriteria(options, matchService);
        ClientPreferenceCriteria clientPreferenceCriteria = new ClientPreferenceCriteria(options);
        EatingCriteria eatingCriteria = new EatingCriteria(options, matchService);
        ExpectationCriteria expectationCriteria = new ExpectationCriteria(options, matchService);
        HealthOverallCriteria healthOverallCriteria = new HealthOverallCriteria(options, matchService);
        PatientExhaustedCriteria patientExhaustedCriteria = new PatientExhaustedCriteria(options, matchService);
        ProblemCriteria problemCriteria = new ProblemCriteria(options, matchService);
        SleepCriteria sleepCriteria = new SleepCriteria(options, matchService);
//        AvailabilityCriteria availabilityCriteria = new AvailabilityCriteria(availability, availabilityService);

        return List.of(
                apathyCriteria,
                clientPreferenceCriteria,
                eatingCriteria,
                expectationCriteria,
                healthOverallCriteria,
                patientExhaustedCriteria,
                problemCriteria,
                sleepCriteria);
    }
}
