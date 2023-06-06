package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.Availability;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.service.AvailabilityService;
import lombok.Builder;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Builder
public class AvailabilityCriteria implements Criteria {
    private final Availability availability;
    private final AvailabilityService availabilityService;

    public AvailabilityCriteria(Availability patientAvailability, AvailabilityService availabilityService) {
        this.availability = patientAvailability;
        this.availabilityService = availabilityService;
    }

    @Override
    public Collection<User> meetCriteria(Collection<User> psychologists) {
        return psychologists.stream()
                .filter(this::meetCriteria)
                .collect
                        (Collectors.toCollection(
                                () -> new TreeSet<>(Comparator.comparing(User::getUsername))));
    }

    private boolean meetCriteria(User psychologist) {
        return psychologist.getAvailabilities().stream()
                .anyMatch(a -> this.availabilityService.isIntersecting(a, this.availability));
    }
}
