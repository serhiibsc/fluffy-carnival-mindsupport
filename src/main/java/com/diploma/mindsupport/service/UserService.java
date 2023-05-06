package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.RegisterRequest;
import com.diploma.mindsupport.dto.UpdateUserInfoRequest;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.mapper.RegisterRequestMapperImpl;
import com.diploma.mindsupport.mapper.UserInfoMapperImpl;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserRole;
import com.diploma.mindsupport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RegisterRequestMapperImpl registerRequestMapper;
    private final UserInfoMapperImpl userInfoMapper;

    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        User user = getUserOrThrow(updateUserInfoRequest.getUsername());
        user.setUserInfo(userInfoMapper.userInfoDtoToUserInfo(updateUserInfoRequest.getUserInfoDto()));
        userRepository.save(user);
    }

    public void savePatient(RegisterRequest request) {
        saveUser(request, UserRole.PATIENT);
    }

    public UserProfileInfoResponse getUserProfileInfo(String username) {
        User user = getUserOrThrow(username);
        return userInfoMapper.userToUserProfileInfo(user);
    }

    /**
     * Just saves user by given RegisterRequest and UserRole WITH password encryption.
     * @param request
     * @param role
     */
    private void saveUser(RegisterRequest request, UserRole role) {
        User user = registerRequestMapper.registerRequestToUser(request, role, passwordEncoder);
        userRepository.save(user);
    }

    private User getUserOrThrow(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return userOptional.get();
    }
}
