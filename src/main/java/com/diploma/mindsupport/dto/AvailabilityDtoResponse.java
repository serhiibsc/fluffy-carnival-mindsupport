package com.diploma.mindsupport.dto;


import com.diploma.mindsupport.model.AvailabilityRecurrence;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class AvailabilityDtoResponse {
    private UserProfileInfoResponse user;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private AvailabilityRecurrence recurrence;
    private ZonedDateTime recurrenceEndDate;
}
