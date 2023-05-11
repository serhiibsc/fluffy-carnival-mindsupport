package com.diploma.mindsupport.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
public class AppointmentDtoResponse {
    private ZonedDateTime dateTime;
    private Duration duration;
    private ZonedDateTime createdAt;
    private String zoomLink;
    private UserProfileInfoResponse scheduledBy;

    private Set<UserProfileInfoResponse> attendees;
}
