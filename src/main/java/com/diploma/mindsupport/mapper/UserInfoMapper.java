package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.UserInfoDto;
import com.diploma.mindsupport.dto.UserProfileInfoResponse;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(target = "photo.data",
            expression = "java(userInfoDto.getImageBase64Data() != null ? java.util.Base64.getDecoder().decode(userInfoDto.getImageBase64Data()) : null)")
    UserInfo userInfoDtoToUserInfo(UserInfoDto userInfoDto);

    @Mapping(target = "firstName", source = "user.userInfo.firstName")
    @Mapping(target = "lastName", source = "user.userInfo.lastName")
    @Mapping(target = "phone", source = "user.userInfo.phone")
    @Mapping(target = "dateOfBirth", source = "user.userInfo.dateOfBirth", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "gender", source = "user.userInfo.gender")
    @Mapping(target = "language", source = "user.userInfo.language")
    @Mapping(target = "country", source = "user.userInfo.country")
    @Mapping(target = "city", source = "user.userInfo.city")
    @Mapping(target = "about", source = "user.userInfo.about")
    @Mapping(target = "image.base64Data",
            expression = "java(user.getUserInfo().getPhoto().getData() != null ? java.util.Base64.getEncoder().encodeToString(user.getUserInfo().getPhoto().getData()) : null)")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "userRole",
            expression = "java((user.getAuthorities() != null && !user.getAuthorities().isEmpty()) ? (com.diploma.mindsupport.model.UserRole) user.getAuthorities().iterator().next() : null)")
    UserProfileInfoResponse userToUserProfileInfo(User user);
}
