package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.CreateAvailabilityRequest;
import com.diploma.mindsupport.dto.UpdateAvailabilityRequest;
import com.diploma.mindsupport.mapper.AvailabilityMapperImpl;
import com.diploma.mindsupport.model.Availability;
import com.diploma.mindsupport.model.AvailabilityRecurrence;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final UserService userService;
    private final AvailabilityMapperImpl availabilityMapper;

    public Availability createAvailabilityForCurrentUser(String currentUsername,
                                                         CreateAvailabilityRequest createAvailabilityRequest) {
        checkIfUserCanActOrThrow(currentUsername, createAvailabilityRequest.getUsername());
        Availability availability = availabilityMapper.toAvailability(createAvailabilityRequest);
        User user = userService.getUserByUsernameOrThrow(currentUsername);
        doesAvailabilityIntercept(user, availability.getStartDateTime(), availability.getEndDateTime());
        availability.setUser(user);
        return availabilityRepository.save(availability);
    }

    public void deleteAvailabilityForCurrentUser(String username, Long availabilityId) {
        Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new IllegalArgumentException("Availability not found"));
        checkIfUserCanActOrThrow(username, availability.getUser().getUsername());

        availabilityRepository.delete(availability);
    }

    public Availability updateAvailability(Long availabilityId, UpdateAvailabilityRequest request, String currentUsername) {
        checkIfUserCanActOrThrow(currentUsername, request.getUsername());
        Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new IllegalArgumentException("Availability not found"));

        if (doesAvailabilityIntercept(availability.getUser(), request.getStartDateTime(), request.getEndDateTime())) {
            throw new IllegalStateException("The new availability intercepts with an existing one.");
        }

        availability.setStartDateTime(request.getStartDateTime());
        availability.setEndDateTime(request.getEndDateTime());
        availability.setRecurrence(request.getRecurrence());
        availability.setRecurrenceEndDate(request.getRecurrenceEndDate());

        return availabilityRepository.save(availability);
    }

    public List<Availability> getAvailabilitiesForUser(String username, ZonedDateTime from, ZonedDateTime to) {
        User user = userService.getUserByUsernameOrThrow(username);
        List<Availability> availabilities = availabilityRepository.findByUser(user);
        List<Availability> result = new ArrayList<>();

        for (Availability availability : availabilities) {
            result.addAll(generateSlots(availability, from, to));
        }

        return result;
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

    private boolean doesAvailabilityIntercept(User user, ZonedDateTime newStart, ZonedDateTime newEnd) {
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

    private void checkIfUserCanActOrThrow(String currentUsername, String usernameFromRequest) {
        if (!currentUsername.equals(usernameFromRequest)) {
            throw new IllegalStateException("User is not authorized");
        }
    }
}
