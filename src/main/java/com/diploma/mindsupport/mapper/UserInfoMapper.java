package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.UserInfoDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.model.Image;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserInfo;
import com.diploma.mindsupport.model.UserRole;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfo userInfoDtoToUserInfo(UserInfoDto userInfoDto);

    default UserProfileInfoResponse userToUserProfileInfo(User user) {
        if (user == null) {
            return null;
        }

        UserProfileInfoResponse.UserProfileInfoResponseBuilder userProfileInfoResponse = UserProfileInfoResponse.builder();

        userProfileInfoResponse.userRole(userToUserRole(user));
        userProfileInfoResponse.lastName(user.getLastName());
        userProfileInfoResponse.username(user.getUsername());
        userProfileInfoResponse.email(user.getEmail());
        userProfileInfoResponse.firstName(user.getFirstName());

        if (user.getUserInfo() == null) {
            return userProfileInfoResponse.build();
        }

        userProfileInfoResponse.dateOfBirth(user.getUserInfo().getDateOfBirth());
        userProfileInfoResponse.gender(user.getUserInfo().getGender());
        userProfileInfoResponse.language(user.getUserInfo().getLanguage());
        userProfileInfoResponse.country(user.getUserInfo().getCountry());
        userProfileInfoResponse.city(user.getUserInfo().getCity());
        userProfileInfoResponse.about(user.getUserInfo().getAbout());
        userProfileInfoResponse.imageList(userPhotoToImageList(user.getUserInfo().getPhoto()));

        return userProfileInfoResponse.build();
    }

    private List<Integer> userPhotoToImageList(Image photo) {
        byte[] imageByteArray = photo.getData();
        List<Integer> imageList = new ArrayList<>(imageByteArray.length);
        for (byte b : imageByteArray) {
            imageList.add((int) b & 0xFF);
        }
        return imageList;
    }

    private UserRole userToUserRole(User user) {
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            return null;
        }
        return (UserRole) user.getAuthorities().iterator().next();
    }
}
