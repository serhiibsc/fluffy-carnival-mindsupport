package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Availability;
import com.diploma.mindsupport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByUser(User user);
}
