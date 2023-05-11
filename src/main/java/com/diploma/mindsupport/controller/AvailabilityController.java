package com.diploma.mindsupport.controller;


import com.diploma.mindsupport.dto.AvailabilityDtoResponse;
import com.diploma.mindsupport.dto.UpdateAvailabilityRequest;
import com.diploma.mindsupport.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityDtoResponse> getAvailabilityById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        AvailabilityDtoResponse response = availabilityService.getAvailabilityById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityDtoResponse> updateAvailability(
            @PathVariable("id") Long id,
            @RequestBody UpdateAvailabilityRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!request.getUsername().equals(userDetails.getUsername())) {
            throw new IllegalStateException("User is not authorized for this request");
        }
        return ResponseEntity.ok(availabilityService.updateAvailability(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAvailability(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        availabilityService.deleteAvailabilityForCurrentUser(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
