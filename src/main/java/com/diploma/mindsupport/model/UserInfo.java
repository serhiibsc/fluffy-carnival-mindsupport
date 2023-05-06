package com.diploma.mindsupport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO: add availability hours to scheduling service
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "user_profiles")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 176672693508487132L;

    @Id
    private Long userId;

    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    private String language;
    private String about;
    private String city;
    private String country;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "image_id",
            referencedColumnName = "imageId")
    private Image photo;
}
