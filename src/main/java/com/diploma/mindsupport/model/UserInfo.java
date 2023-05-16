package com.diploma.mindsupport.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "user_profiles")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 176672693508487132L;

    @Id
    private Long userId;

    @EqualsAndHashCode.Exclude
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String language;
    private String about;
    private String city;
    private String country;
    private String phone;

    @EqualsAndHashCode.Exclude
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "image_id",
            referencedColumnName = "imageId")
    private Image photo;
}
