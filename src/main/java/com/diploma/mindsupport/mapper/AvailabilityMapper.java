package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.AvailabilityDtoResponse;
import com.diploma.mindsupport.dto.CreateAvailabilityRequest;
import com.diploma.mindsupport.model.Availability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserInfoMapper.class)
public interface AvailabilityMapper {
    Availability toAvailability(CreateAvailabilityRequest request);

    @Mapping(source = "user", target = "user")
    AvailabilityDtoResponse toAvailabilityDtoResponse(Availability availability);

    List<AvailabilityDtoResponse> toAvailabilityDtoResponse(List<Availability> availabilities);
}
