package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
