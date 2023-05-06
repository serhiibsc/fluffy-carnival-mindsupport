package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.ImageDto;
import com.diploma.mindsupport.model.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    default Image imageDtoToImage(ImageDto imageDto) {
        List<Integer> imageList = imageDto.getImage();
        byte[] imageByteArray = new byte[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            imageByteArray[i] = imageList.get(i).byteValue();
        }
        return Image.builder().data(imageByteArray).build();
    }
}
