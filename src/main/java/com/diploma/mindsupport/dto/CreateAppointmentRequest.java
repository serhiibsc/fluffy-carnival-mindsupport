package com.diploma.mindsupport.dto;

import com.diploma.mindsupport.helper.DurationDeserializer;
import com.diploma.mindsupport.helper.DurationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CreateAppointmentRequest {
    private ZonedDateTime startTime;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;
    private List<String> attendeesUsernames;
}
