package com.dailyalcorcode.buynowdotcom.service.image;

import com.dailyalcorcode.buynowdotcom.dtos.ImageDto;
import com.dailyalcorcode.buynowdotcom.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long imageId);

    void deleteImage(Long imageId);

    void updateImage(Long imageId, MultipartFile file);

    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
}
