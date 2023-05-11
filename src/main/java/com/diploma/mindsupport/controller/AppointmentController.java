package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.AppointmentDtoResponse;
import com.diploma.mindsupport.dto.CreateAppointmentRequest;
import com.diploma.mindsupport.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentDtoResponse>> getCurrentUserAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByUsername(userDetails.getUsername()));
    }

    @PostMapping("/my/create")
    public ResponseEntity<AppointmentDtoResponse> createAppointmentWithCurrentUser(
            @RequestBody CreateAppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        AppointmentDtoResponse created = appointmentService.createAppointmentByCurrentlyAuthorizedUser(request, userDetails.getUsername());
        return ResponseEntity.ok(created);
    }
}
