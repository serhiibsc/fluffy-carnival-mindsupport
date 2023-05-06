package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.RegisterRequest;
import com.diploma.mindsupport.dto.UpdateUserInfoRequest;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('VOLUNTEER')")
    public ResponseEntity<UserProfileInfoResponse> getCurrentUserProfileInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        throwIfNotAuthorized(username);
        return ResponseEntity.ok(userService.getUserProfileInfo(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileInfoResponse> getUserProfileInfo(@PathVariable("username") String username) {
        // todo: consider additional security
        return ResponseEntity.ok(userService.getUserProfileInfo(username));
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateCurrentUser(
            @RequestBody UpdateUserInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        throwIfNotAuthorized(authentication.getName());
        userService.updateUserInfo(request);
        return ResponseEntity.ok("User successfully created!");
    }

    private void throwIfNotAuthorized(String username) {
        if (Objects.isNull(username)) {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }
}
