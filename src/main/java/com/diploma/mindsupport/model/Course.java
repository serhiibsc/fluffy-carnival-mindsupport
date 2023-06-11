package com.diploma.mindsupport.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseId;

    @Column
    private ZonedDateTime createdAt = ZonedDateTime.now();

    private String courseName;

    @EqualsAndHashCode.Exclude
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", referencedColumnName = "userId")
    private User therapist;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.ALL)
    private List<User> attendees;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();
}
