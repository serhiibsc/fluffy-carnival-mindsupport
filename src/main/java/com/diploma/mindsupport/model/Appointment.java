package com.diploma.mindsupport.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Set;

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
    private ZonedDateTime startTime;

    @Transient
    private Duration duration;

    @Column(name = "duration")
    private Long durationSeconds;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (duration != null) {
            durationSeconds = duration.getSeconds();
        }
    }

    @PostLoad
    public void postLoad() {
        if (durationSeconds != null) {
            duration = Duration.ofSeconds(durationSeconds);
        }
    }

    @Column(columnDefinition = "timestamp")
    private final ZonedDateTime createdAt = ZonedDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_by_user_id")
    private User scheduledBy;

    @ManyToMany
    @JoinTable(name = "appointment_users",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> attendees;
}
