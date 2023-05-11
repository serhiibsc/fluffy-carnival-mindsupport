package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.CreateAvailabilityRequest;
import com.diploma.mindsupport.model.Availability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {
    Availability toAvailability(CreateAvailabilityRequest request);
}
