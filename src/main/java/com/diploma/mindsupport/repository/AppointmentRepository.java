package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
