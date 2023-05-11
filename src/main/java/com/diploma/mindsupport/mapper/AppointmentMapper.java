package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.AppointmentDtoResponse;
import com.diploma.mindsupport.dto.CreateAppointmentRequest;
import com.diploma.mindsupport.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "scheduledBy", target = "scheduledBy")
    @Mapping(source = "attendees", target = "attendees")
    AppointmentDtoResponse toAppointmentDto(Appointment appointment);

    default Appointment toAppointment(CreateAppointmentRequest request) {
        if (request == null) {
            return null;
        }

        Appointment.AppointmentBuilder appointment = Appointment.builder();
        appointment.duration(request.getDuration());
        appointment.startTime(request.getStartTime());

        return appointment.build();
    }

    List<AppointmentDtoResponse> toAppointmentDto(Collection<Appointment> appointmentCollection);
}
