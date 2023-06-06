package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByGrantedAuthoritiesContains(UserRole userRole);
}

