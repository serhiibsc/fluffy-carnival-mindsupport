package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.AvailabilityDtoResponse;
import com.diploma.mindsupport.dto.CreateAvailabilityRequest;
import com.diploma.mindsupport.dto.UpdateAvailabilityRequest;
import com.diploma.mindsupport.mapper.AvailabilityMapperImpl;
import com.diploma.mindsupport.model.Availability;
import com.diploma.mindsupport.model.AvailabilityRecurrence;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final UserService userService;
    private final AvailabilityMapperImpl availabilityMapper;

    public AvailabilityDtoResponse createAvailabilityForUser(
            String username, CreateAvailabilityRequest createAvailabilityRequest) {
        checkIfUserCanActOrThrow(username, createAvailabilityRequest.getUsername());
        Availability availability = availabilityMapper.toAvailability(createAvailabilityRequest);
        User user = userService.getUserByUsernameOrThrow(username);
        isUserAvailable(user, availability.getStartDateTime(), availability.getEndDateTime());
        availability.setUser(user);
        return availabilityMapper.toAvailabilityDtoResponse(availabilityRepository.save(availability));
    }

    public void deleteAvailabilityForCurrentUser(Long availabilityId, String username) {
        Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new IllegalArgumentException("Availability not found"));
        checkIfUserCanActOrThrow(username, availability.getUser().getUsername());

        availabilityRepository.delete(availability);
    }

    public AvailabilityDtoResponse updateAvailability(
            Long availabilityId, UpdateAvailabilityRequest request) {
        Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new IllegalArgumentException("Availability not found"));

        if (isUserAvailable(availability.getUser(), request.getStartDateTime(), request.getEndDateTime())) {
            throw new IllegalStateException("The new availability intercepts with an existing one.");
        }

        availability.setStartDateTime(request.getStartDateTime());
        availability.setEndDateTime(request.getEndDateTime());
        availability.setRecurrence(request.getRecurrence());
        availability.setRecurrenceEndDate(request.getRecurrenceEndDate());

        return availabilityMapper.toAvailabilityDtoResponse(availabilityRepository.save(availability));
    }

    public List<AvailabilityDtoResponse> getAvailabilitiesForUser(String username, ZonedDateTime from, ZonedDateTime to) {
        User user = userService.getUserByUsernameOrThrow(username);
        List<Availability> availabilities = availabilityRepository.findByUser(user);
        List<Availability> result = new ArrayList<>();

        for (Availability availability : availabilities) {
            result.addAll(generateSlots(availability, from, to));
        }

        return availabilityMapper.toAvailabilityDtoResponse(result);
    }

    public boolean isUserAvailable(User user, ZonedDateTime newStart, ZonedDateTime newEnd) {
        List<Availability> availabilities = availabilityRepository.findByUser(user);
        for (Availability availability : availabilities) {
            List<Availability> slots = generateSlots(availability, newStart, newEnd);
            for (Availability slot : slots) {
                if (slot.getStartDateTime().isBefore(newEnd) && slot.getEndDateTime().isAfter(newStart)) {
                    return true;
                }
            }
        }
        return false;
    }

    public AvailabilityDtoResponse getAvailabilityById(Long id, String username) {
        Optional<Availability> optionalAvailability = availabilityRepository.findById(id);
        Availability availability = optionalAvailability.orElseThrow();
        if (!availability.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("User is not authorized for this resource");
        }
        return availabilityMapper.toAvailabilityDtoResponse(availability);
    }

    private List<Availability> generateSlots(Availability availability, ZonedDateTime from, ZonedDateTime to) {
        List<Availability> slots = new ArrayList<>();

        ZonedDateTime start = availability.getStartDateTime();
        ZonedDateTime end = availability.getEndDateTime();
        AvailabilityRecurrence recurrence = availability.getRecurrence();
        ZonedDateTime recurrenceEnd = availability.getRecurrenceEndDate();

        while (start.isBefore(to) && (recurrenceEnd == null || start.isBefore(recurrenceEnd))) {
            if (start.isAfter(from)) {
                Availability slot = new Availability();
                slot.setUser(availability.getUser());
                slot.setStartDateTime(start);
                slot.setEndDateTime(end);
                slots.add(slot);
            }

            switch (recurrence) {
                case ONCE:
                    return slots;
                case DAILY:
                    start = start.plusDays(1);
                    end = end.plusDays(1);
                    break;
                case WEEKLY:
                    start = start.plusWeeks(1);
                    end = end.plusWeeks(1);
                    break;
                case FORTNIGHTLY:
                    start = start.plusWeeks(2);
                    end = end.plusWeeks(2);
                    break;
                case WEEKDAYS:
                    do {
                        start = start.plusDays(1);
                        end = end.plusDays(1);
                    } while (start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY);
                    break;
                case WEEKENDS:
                    do {
                        start = start.plusDays(1);
                        end = end.plusDays(1);
                    } while (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown recurrence: " + recurrence);
            }
        }

        return slots;
    }

    private void checkIfUserCanActOrThrow(String currentUsername, String usernameFromRequest) {
        if (!currentUsername.equals(usernameFromRequest)) {
            throw new IllegalStateException("User is not authorized");
        }
    }
}
