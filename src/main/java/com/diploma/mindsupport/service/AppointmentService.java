package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.AppointmentDtoResponse;
import com.diploma.mindsupport.dto.CreateAppointmentRequest;
import com.diploma.mindsupport.mapper.AppointmentMapper;
import com.diploma.mindsupport.model.Appointment;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;
    private final ZoomService zoomService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<AppointmentDtoResponse> getAllAppointmentsByUsername(String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        List<Appointment> appointments = appointmentRepository.getAppointmentsByAttendeesContains(user);
        return appointmentMapper.toAppointmentDto(appointments);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public AppointmentDtoResponse createAppointmentByCurrentlyAuthorizedUser(
            CreateAppointmentRequest createAppointmentRequest, String currentlyAuthorizedUsername) {
        Appointment appointment = createAppointment(createAppointmentRequest, currentlyAuthorizedUsername);
        // todo: 1. check availability. 2. check already scheduled meetings
        return appointmentMapper.toAppointmentDto(appointmentRepository.save(appointment));
    }

    public Appointment updateAppointment(Long id, Appointment appointment) {
        Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(id);
        if (existingAppointmentOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Appointment %d not found", id));
        }
        appointment.setAppointmentId(id);
        // todo: update zoom meeting
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        // todo: delete zoom meeting
        appointmentRepository.deleteById(id);
    }

    public void addAttendee(Long appointmentId, String username) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Appointment %d not found", appointmentId)));
        User user = userService.getUserByUsernameOrThrow(username);

        appointment.getAttendees().add(user);
        appointmentRepository.save(appointment);
    }

    private Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest, String currentUsername) {
        User currentUser = userService.getUserByUsernameOrThrow(currentUsername);
        Appointment appointment = appointmentMapper.toAppointment(createAppointmentRequest);
        appointment.setAttendees(new TreeSet<>(Comparator.comparing(User::getUsername)));
        for (String username : createAppointmentRequest.getAttendeesUsernames()) {
            appointment.getAttendees().add(userService.getUserByUsernameOrThrow(username));
        }
        appointment.setScheduledBy(currentUser);

        String response = getCreateZoomMeetingResponse(currentUser, createAppointmentRequest, zoomService);

        appointment.setZoomLink(getZoomLink(response));
        appointment.setZoomMeetingId(0L);// todo: set correct id
        return appointment;
    }

    private String getCreateZoomMeetingResponse(User user, CreateAppointmentRequest request, ZoomService zoomService) {
        return zoomService.createMeeting(
                user.getUserId().toString(),
                "",
                request.getStartTime(),
                request.getDuration()).toString();
    }

    private String getZoomLink(String response) {
        // todo: set correct regex
        Matcher matcher = Pattern.compile("").matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
