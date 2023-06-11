package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.OptionDto;
import com.diploma.mindsupport.mapper.OptionMapper;
import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OptionService {
    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    public Optional<Option> getOptionById(Long id) {
        return optionRepository.findById(id);
    }

    public Option getOptionByIdOrThrow(Long id) {
        return getOptionById(id).orElseThrow();
    }

    public List<Option> getOptionsFromDto(List<OptionDto> optionDtoList) {
        return optionDtoList.stream().map(dto -> getOptionById(dto.getId()).orElseThrow()).toList();
    }

    public List<Option> getOptionsByIds(List<Long> optionIds) {
        return optionIds.stream().map(this::getOptionByIdOrThrow).toList();
    }
}
