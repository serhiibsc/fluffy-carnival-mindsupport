package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Appointment;
import com.diploma.mindsupport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> getAppointmentsByAttendeesContains(User user);
}
