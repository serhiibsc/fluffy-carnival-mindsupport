package com.diploma.mindsupport.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CreateAppointmentRequest {
    private ZonedDateTime startTime;
    private Duration duration;
    private List<String> attendeesUsernames;
}
