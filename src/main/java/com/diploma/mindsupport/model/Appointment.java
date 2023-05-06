package com.diploma.mindsupport.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "appointments")
public class Appointment implements Serializable {
    @Serial
    private static final long serialVersionUID = 6095626308020191300L;

    @Id
    @SequenceGenerator(
            name = "appointment_sequence",
            sequenceName = "appointment_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_sequence")
    private Long appointmentId;

    @Column(columnDefinition = "timestamptz")
    private ZonedDateTime dateTime;
    @Column(columnDefinition = "interval")
    private Duration duration;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    private String zoomLink;

    private Long scheduledBy;

    @ManyToMany
    @JoinTable(name = "appointment_users",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> attendees;
}