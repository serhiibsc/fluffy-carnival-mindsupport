package com.diploma.mindsupport.repository;

import com.diploma.mindsupport.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
