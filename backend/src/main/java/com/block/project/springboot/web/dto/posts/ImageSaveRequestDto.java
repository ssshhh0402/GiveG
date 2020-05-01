package com.block.project.springboot.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class ImageSaveRequestDto {
    MultipartFile image;

    public ImageSaveRequestDto(MultipartFile iamge){
        this.image = iamge;
    }
}
