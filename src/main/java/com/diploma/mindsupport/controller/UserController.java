package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.ImageDto;
import com.diploma.mindsupport.dto.UserInfoDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.service.UserInfoService;
import com.diploma.mindsupport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserInfoService userInfoService;

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
}
