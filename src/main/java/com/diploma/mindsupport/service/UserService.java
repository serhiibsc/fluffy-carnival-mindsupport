package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.RegisterRequest;
import com.diploma.mindsupport.dto.UserInfoDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.mapper.RegisterRequestMapperImpl;
import com.diploma.mindsupport.mapper.UserInfoMapperImpl;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserInfo;
import com.diploma.mindsupport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RegisterRequestMapperImpl registerRequestMapper;
    private final UserInfoMapperImpl userInfoMapper;

    public void updateUserInfo(UserInfoDto userInfoDto, String username) {
        User user = getUserByUsernameOrThrow(username);
        UserInfo userInfo = userInfoMapper.userInfoDtoToUserInfo(userInfoDto);
        userInfo.setUserId(user.getUserId());
        user.setUserInfo(userInfo);
        userRepository.save(user);
    }

    public UserProfileInfoResponse getUserProfileInfo(String username) {
        User user = getUserByUsernameOrThrow(username);
        return userInfoMapper.userToUserProfileInfo(user);
    }

    /**
     * Just saves user by given RegisterRequest and UserRole WITH password encryption.
     * @param request
     */
    public void saveUser(RegisterRequest request) {
        User user = registerRequestMapper.registerRequestToUser(request, passwordEncoder);
        userRepository.save(user);
    }

    public User getUserByUsernameOrThrow(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return userOptional.get();
    }
}
