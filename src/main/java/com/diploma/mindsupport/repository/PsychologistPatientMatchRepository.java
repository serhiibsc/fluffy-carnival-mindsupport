package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.PsychologistPatientMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologistPatientMatchRepository extends JpaRepository<PsychologistPatientMatch, Long> {
    List<PsychologistPatientMatch> findByPatientOption(Option patientOption);
    List<PsychologistPatientMatch> findByPsychologistOption(Option psychologistOption);
}