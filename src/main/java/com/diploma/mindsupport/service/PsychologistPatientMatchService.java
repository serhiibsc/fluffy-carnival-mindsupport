package com.diploma.mindsupport.service;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.PsychologistPatientMatch;
import com.diploma.mindsupport.repository.PsychologistPatientMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PsychologistPatientMatchService {
    private final PsychologistPatientMatchRepository repository;

    public List<PsychologistPatientMatch> getByPatientOption(Option option) {
        return repository.findByPatientOption(option);
    }

    public void saveMatches(List<PsychologistPatientMatch> matches) {
        repository.saveAll(matches);
    }

    public List<Option> getPsychologistOptionsByPatientOptions(List<Option> patientOptions) {
        return patientOptions.stream()
                .flatMap(o -> getByPatientOption(o).stream())
                .map(PsychologistPatientMatch::getPsychologistOption)
                .toList();
    }
}
