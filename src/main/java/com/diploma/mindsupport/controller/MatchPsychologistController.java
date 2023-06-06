package com.diploma.mindsupport.controller;


import com.diploma.mindsupport.dto.MatchPsychologistsRequest;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matcher")
@RequiredArgsConstructor
public class MatchPsychologistController {
    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<List<UserProfileInfoResponse>> matchPsychologists(@RequestBody MatchPsychologistsRequest request) {
        return ResponseEntity.ok(matchingService.matchPsychologists(request.getOptionDtoList()));
    }
}
