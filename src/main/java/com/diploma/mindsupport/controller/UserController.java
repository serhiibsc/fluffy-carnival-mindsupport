package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.*;
import com.diploma.mindsupport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final AvailabilityService availabilityService;
    private final AppointmentService appointmentService;
    private final UserAnswerService userAnswerService;
    private final MatchingService matchingService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileInfoResponse> getCurrentUserProfileInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserProfileInfo(userDetails.getUsername()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileInfoResponse> getUserProfileInfo(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.getUserProfileInfo(username));
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateCurrentUser(
            @RequestBody UserInfoDto userInfoDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserInfo(userInfoDto, userDetails.getUsername());
        return ResponseEntity.ok("User successfully updated!");
    }

    @PatchMapping("/me/photo")
    public ResponseEntity<String> updateCurrentUserPhoto(
            @RequestBody ImageDto imageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        userInfoService.updateUserPhoto(imageDto, userDetails.getUsername());
        return ResponseEntity.ok("User photo successfully updated!");
    }

    @PreAuthorize("authentication.principal.username == #username")
    @GetMapping("/{username}/availabilities")
    public ResponseEntity<List<AvailabilityDtoResponse>> getAvailabilitiesForUser(
            @PathVariable("username") String username,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime zonedTimeFrom,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime zonedTimeTo) {
        List<AvailabilityDtoResponse> response =
                availabilityService.getAvailabilitiesForUser(username, zonedTimeFrom, zonedTimeTo);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("authentication.principal.username == #username")
    @PatchMapping("/{username}/availabilities")
    public ResponseEntity<AvailabilityDtoResponse> createAvailability(
            @PathVariable("username") String username,
            @RequestBody CreateAvailabilityRequest createAvailabilityRequest) {
        AvailabilityDtoResponse response = availabilityService.createAvailabilityForUser(
                username, createAvailabilityRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getAvailabilityId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("authentication.principal.username == #username")
    @GetMapping("/{username}/appointments")
    public ResponseEntity<List<AppointmentDtoResponse>> getUserAppointments(
            @PathVariable("username") String username) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByUsername(username));
    }

    @PreAuthorize("authentication.principal.username == #username")
    @PatchMapping("/{username}/appointments")
    public ResponseEntity<AppointmentDtoResponse> createAppointment(
            @PathVariable("username") String username,
            @RequestBody CreateAppointmentRequest request) {
        AppointmentDtoResponse response = appointmentService.createAppointmentForUser(request, username);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getAppointmentId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("authentication.principal.username == #username")
    @PostMapping("/{username}/answer")
    public ResponseEntity<List<UserAnswerDto>> addUserAnswers(
            @PathVariable("username") String username,
            @RequestBody UserAnswersRequest request) {
        return ResponseEntity.ok(userAnswerService.saveUserAnswers(request.getOptionIds(), username));
    }

    @PreAuthorize("authentication.principal.username == #username")
    @PostMapping("/{username}/match")
    public ResponseEntity<List<UserProfileInfoResponse>> matchPsychologists(
            @PathVariable("username") String username,
            @RequestBody MatchPsychologistsRequest request) {
        return ResponseEntity.ok(matchingService.matchPsychologists(request, username));
    }
}
