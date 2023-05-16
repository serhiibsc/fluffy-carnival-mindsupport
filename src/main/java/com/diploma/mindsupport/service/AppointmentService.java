package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.AppointmentDtoResponse;
import com.diploma.mindsupport.dto.CreateAppointmentRequest;
import com.diploma.mindsupport.dto.UpdateAppointmentRequest;
import com.diploma.mindsupport.mapper.AppointmentMapper;
import com.diploma.mindsupport.model.Appointment;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;
    private final AvailabilityService availabilityService;

    public AppointmentDtoResponse getAppointmentById(Long id, String username) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        User user = userService.getUserByUsernameOrThrow(username);
        if (appointmentOptional.isEmpty() || !appointmentOptional.get().getAttendees().contains(user)) {
            throw new IllegalStateException("User is not authorized for this request");
        }
        return appointmentMapper.toAppointmentDto(appointmentOptional.get());
    }

    public List<AppointmentDtoResponse> getAllAppointmentsByUsername(String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        List<Appointment> appointments = appointmentRepository.getAppointmentsByAttendeesContains(user);
        return appointmentMapper.toAppointmentDto(appointments);
    }

    // todo: get appointments scheduled only by some user
    // todo: get appointments where attendees given users

    public AppointmentDtoResponse createAppointmentForUser(CreateAppointmentRequest request, String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        Appointment appointment = buildAppointment(request, user);
        ZonedDateTime endTime = appointment.getStartTime().plus(appointment.getDuration());

        checkIfUsersCanHaveAppointment(appointment, endTime);
        return appointmentMapper.toAppointmentDto(appointmentRepository.save(appointment));
    }

    public AppointmentDtoResponse updateAppointment(Long id, UpdateAppointmentRequest updateAppointmentRequest, String username) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Appointment %d not found", id));
        }
        Appointment appointment = appointmentOptional.get();
        if (!username.equals(appointment.getScheduledBy().getUsername())) {
            throw new IllegalStateException("User is not authorized for this request");
        }

        appointment.setStartTime(updateAppointmentRequest.getStartTime());
        appointment.setDuration(updateAppointmentRequest.getDuration());
        appointment.setAttendees(getAttendeesByUsernames(updateAppointmentRequest.getAttendeesUsernames()));

        ZonedDateTime endTime = appointment.getStartTime().plus(appointment.getDuration());
        checkIfUsersCanHaveAppointment(appointment, endTime);
        return appointmentMapper.toAppointmentDto(appointmentRepository.save(appointment));
    }

    public void deleteAppointment(Long id) {
        Appointment availability = appointmentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Appointment not found"));
        appointmentRepository.delete(availability);
        // todo: delete zoom meeting
    }

    public boolean doesUserHaveAppointment(User user, ZonedDateTime startTime, ZonedDateTime endTime) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByAttendeesContains(user);
        for (Appointment appointment : appointments) {
            boolean appointmentStartBeforeEndTime = appointment.getStartTime().isBefore(endTime);
            boolean appointmentEndIsAfterStartTime = appointment.getStartTime().plus(appointment.getDuration())
                    .isAfter(startTime);
            boolean appointmentIntercepts = appointmentStartBeforeEndTime && appointmentEndIsAfterStartTime;

            return appointmentIntercepts;
        }

        return false;
    }

    private Appointment buildAppointment(CreateAppointmentRequest createAppointmentRequest, User user) {
        Appointment appointment = appointmentMapper.toAppointment(createAppointmentRequest);
        appointment.setAttendees(getAttendeesByUsernames(createAppointmentRequest.getAttendeesUsernames()));
        appointment.setScheduledBy(user);

        // todo: set Zoom stuff
        return appointment;
    }

    private TreeSet<User> getAttendeesByUsernames(List<String> attendeesUsernames) {
        TreeSet<User> attendees = new TreeSet<>(Comparator.comparing(User::getUsername));
        for (String username : attendeesUsernames) {
            attendees.add(userService.getUserByUsernameOrThrow(username));
        }
        return attendees;
    }

    private void checkIfUsersCanHaveAppointment(Appointment appointment, ZonedDateTime endTime) {
        for (User u : appointment.getAttendees()) {
            if (!availabilityService.isUserAvailable(u, appointment.getStartTime(), endTime)) {
                throw new IllegalStateException(
                        String.format("The user %s is not available at the requested time.", u.getUsername()) );
            }
            if (doesUserHaveAppointment(u, appointment.getStartTime(), endTime)) {
                throw new IllegalStateException(
                        String.format("The user %s already has an appointment at the requested time.", u.getUsername())
                );
            }
        }
    }
}
