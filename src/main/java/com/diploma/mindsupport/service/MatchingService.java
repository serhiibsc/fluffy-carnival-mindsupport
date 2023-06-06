package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.MatchPsychologistsRequest;
import com.diploma.mindsupport.dto.OptionDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.mapper.UserInfoMapper;
import com.diploma.mindsupport.matching.Criteria;
import com.diploma.mindsupport.matching.ProblemCriteria;
import com.diploma.mindsupport.model.Option;
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
    private final UserInfoMapper userInfoMapper;
    private final OptionService optionService;

//    public List<User> matchPsychologists(MatchPsychologistsRequest request) {
//        List<Criteria> criteriaList = createCriteriaList(request);
//        AndCriteria andCriteria = AndCriteria.builder().criteriaList(criteriaList).build();
//        return andCriteria.meetCriteria(userService.getPsychologists()).stream().toList();
//    }
//
    public List<UserProfileInfoResponse> matchPsychologists(List<OptionDto> optionDtoList) {
        List<Option> options = optionService.getOptionsFromDto(optionDtoList);
        ProblemCriteria problemCriteria = new ProblemCriteria(options, matchService);
        return problemCriteria.meetCriteria(userService.getPsychologists()).stream()
                .map(userInfoMapper::userToUserProfileInfo)
                .toList();
    }
    
    public static List<Criteria> createCriteriaList(MatchPsychologistsRequest request) {
        return null;
    }
}
