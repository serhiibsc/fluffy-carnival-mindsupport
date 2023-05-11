package com.diploma.mindsupport.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "availabilities")
public class Availability implements Serializable {
    @Id
    @SequenceGenerator(
            name = "availability_sequence",
            sequenceName = "appointment_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "availability_sequence")
    private Long availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startDateTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endDateTime;

    private AvailabilityRecurrence recurrence;
    private ZonedDateTime recurrenceEndDate;
}
