package com.dailyalcorcode.buynowdotcom.controller;


import com.dailyalcorcode.buynowdotcom.dtos.ImageDto;
import com.dailyalcorcode.buynowdotcom.model.Image;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(@RequestParam("productId") Long productId,
                                                    @RequestParam("files") List<MultipartFile> files) {
        List<ImageDto> imageDto = imageService.saveImages(productId, files);
        return ResponseEntity.ok(new ApiResponse("Images upload successfully!", imageDto));
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"").body(resource);
    }

    //Assigment 2
    /*
        implement the remaining 2 end-points
     */
    // 1. update image

    @PutMapping("/image/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,
                                                   @RequestBody MultipartFile file) {
        imageService.updateImage(imageId, file);
        return ResponseEntity.ok(new ApiResponse("Image updated successfully", null));
    }

    // 2. delete image
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
    }
}
