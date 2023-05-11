package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.*;
import com.diploma.mindsupport.service.AvailabilityService;
import com.diploma.mindsupport.service.UserInfoService;
import com.diploma.mindsupport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
public class UserController {
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final AvailabilityService availabilityService;

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

    @GetMapping("/{username}/availabilities")
    public ResponseEntity<List<AvailabilityDtoResponse>> getAvailabilitiesForUser(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime zonedTimeFrom,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime zonedTimeTo) {
        if (!username.equals(userDetails.getUsername())) {
            throw new IllegalStateException("User is not authorized for this request");
        }
        List<AvailabilityDtoResponse> response =
                availabilityService.getAvailabilitiesForUser(username, zonedTimeFrom, zonedTimeTo);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}/availabilities")
    public ResponseEntity<AvailabilityDtoResponse> createAvailability(
            @PathVariable("username") String username,
            @RequestBody CreateAvailabilityRequest createAvailabilityRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!username.equals(userDetails.getUsername())) {
            throw new IllegalStateException("User is not authorized for this request");
        }
        AvailabilityDtoResponse response = availabilityService.createAvailabilityForUser(
                username, createAvailabilityRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getAvailabilityId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
