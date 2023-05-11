package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.ImageDto;
import com.diploma.mindsupport.mapper.ImageMapper;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserInfo;
import com.diploma.mindsupport.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final ImageMapper imageMapper;

    public void updateUserPhoto(ImageDto imageDto, String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        UserInfo userInfo;
        if (Objects.nonNull(user.getUserInfo())) {
            userInfo = user.getUserInfo();
        } else {
            userInfo = new UserInfo();
            userInfo.setUserId(user.getUserId());
        }
        userInfo.setPhoto(imageMapper.imageDtoToImage(imageDto));
        userInfoRepository.save(userInfo);
    }
}
