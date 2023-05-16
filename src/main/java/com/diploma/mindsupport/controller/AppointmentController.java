package com.diploma.mindsupport.controller;

import com.diploma.mindsupport.dto.AppointmentDtoResponse;
import com.diploma.mindsupport.dto.AvailabilityDtoResponse;
import com.diploma.mindsupport.dto.UpdateAppointmentRequest;
import com.diploma.mindsupport.dto.UpdateAvailabilityRequest;
import com.diploma.mindsupport.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDtoResponse> getAppointmentById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        AppointmentDtoResponse response = appointmentService.getAppointmentById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDtoResponse> updateAppointment(
            @PathVariable("id") Long id,
            @RequestBody UpdateAppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAvailability(@PathVariable("id") Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
