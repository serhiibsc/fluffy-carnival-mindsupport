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
import java.time.Duration;
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
        Availability availability = availabilityMapper.toAvailability(createAvailabilityRequest);
        User user = userService.getUserByUsernameOrThrow(username);
        isUserAvailable(user, availability.getStartDateTime(), availability.getEndDateTime());
        availability.setUser(user);
        return availabilityMapper.toAvailabilityDtoResponse(availabilityRepository.save(availability));
    }

    public void deleteAvailability(Long availabilityId) {
        Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new IllegalArgumentException("Availability not found"));
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

    public AvailabilityDtoResponse getAvailabilityById(Long id, String username) {
        Optional<Availability> optionalAvailability = availabilityRepository.findById(id);
        Availability availability = optionalAvailability.orElseThrow();
        if (!availability.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("User is not authorized for this resource");
        }
        return availabilityMapper.toAvailabilityDtoResponse(availability);
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

    private List<Availability> generateSlots(Availability availability, ZonedDateTime from, ZonedDateTime to) {
        List<Availability> slots = new ArrayList<>();

        ZonedDateTime start = availability.getStartDateTime();
        ZonedDateTime end = availability.getEndDateTime();
        AvailabilityRecurrence recurrence = availability.getRecurrence();
        ZonedDateTime recurrenceEnd = availability.getRecurrenceEndDate();

        from = from.withZoneSameInstant(start.getZone());
        to = to.withZoneSameInstant(start.getZone());

        while (start.isBefore(to) && (recurrenceEnd == null || start.isBefore(recurrenceEnd))) {
            if (start.isBefore(to) && end.isAfter(from)) {
                // Create a new slot that fits within the from and to range
                ZonedDateTime slotStart = start.isAfter(from) ? start : from;
                ZonedDateTime slotEnd = end.isBefore(to) ? end : to;

                Availability slot = new Availability();
                slot.setUser(availability.getUser());
                slot.setStartDateTime(slotStart);
                slot.setEndDateTime(slotEnd);
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

    public boolean isIntersecting(Availability availability1, Availability availability2) {
        ZonedDateTime start1 = availability1.getStartDateTime();
        ZonedDateTime end1 = availability1.getEndDateTime();
        ZonedDateTime start2 = availability2.getStartDateTime();
        ZonedDateTime end2 = availability2.getEndDateTime();

        // Check if the end time of the first availability is before the start time of the second availability
        if (end1.isBefore(start2)) {
            return false;
        }

        // Check if the start time of the first availability is after the end time of the second availability
        if (start1.isAfter(end2)) {
            return false;
        }

        // If none of the above conditions are met, there is an intersection
        return true;
    }

    private List<Availability> generateSlotsForIntersection(Availability availability1, Availability availability2) {
        ZonedDateTime start1 = availability1.getStartDateTime();
        ZonedDateTime end1 = availability1.getEndDateTime();
        ZonedDateTime start2 = availability2.getStartDateTime();
        ZonedDateTime end2 = availability2.getEndDateTime();

        // Determine the start and end times for the overlapping period
        ZonedDateTime intersectionStart = start1.isBefore(start2) ? start2 : start1;
        ZonedDateTime intersectionEnd = end1.isBefore(end2) ? end1 : end2;

        // Create slots within the overlapping period
        List<Availability> slots = new ArrayList<>();
        ZonedDateTime slotStart = intersectionStart;
        while (slotStart.isBefore(intersectionEnd)) {
            long durationMinutes = Duration.between(
                    availability1.getEndDateTime(), availability1.getStartDateTime()).toMinutes();
            ZonedDateTime slotEnd = slotStart.plusMinutes(durationMinutes); // Adjust duration as needed
            Availability slot = new Availability();
            slot.setUser(availability1.getUser());
            slot.setStartDateTime(slotStart);
            slot.setEndDateTime(slotEnd);
            slots.add(slot);
            slotStart = slotEnd;
        }

        return slots;
    }
}
