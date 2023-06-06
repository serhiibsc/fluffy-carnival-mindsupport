package com.diploma.mindsupport.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "psychologist_patient_question_options_match")
public class PsychologistPatientMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_option_id", nullable = false)
    private Option patientOption;

    @ManyToOne
    @JoinColumn(name = "psychologist_option_id", nullable = false)
    private Option psychologistOption;
}
