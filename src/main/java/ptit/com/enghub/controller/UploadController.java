package ptit.com.enghub.controller;

import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.UploadResponse;
import ptit.com.enghub.service.CloudinaryService;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ApiResponse<UploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<UploadResponse>builder()
                .result(cloudinaryService.uploadImage(file))
                .message("Upload image successfully")
                .build();
    }

    @PostMapping("/video")
    public ApiResponse<UploadResponse> uploadVideo(
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<UploadResponse>builder()
                .result(cloudinaryService.uploadVideo(file))
                .message("Upload video successfully")
                .build();
    }

    @PostMapping("/audio")
    public ApiResponse<UploadResponse> uploadAudio(
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<UploadResponse>builder()
                .result(cloudinaryService.uploadAudio(file))
                .message("Upload audio successfully")
                .build();
    }


    @DeleteMapping("/image")
    public ApiResponse<Void> deleteImage(
            @RequestParam String publicId
    ) {
        cloudinaryService.deleteImage(publicId);

        return ApiResponse.<Void>builder()
                .message("Delete image successfully")
                .build();
    }

    @DeleteMapping("/video")
    public ApiResponse<Void> deleteVideo(
            @RequestParam String publicId
    ) {
        cloudinaryService.deleteVideo(publicId);

        return ApiResponse.<Void>builder()
                .message("Delete video successfully")
                .build();
    }

    @DeleteMapping("/audio")
    public ApiResponse<Void> deleteAudio(
            @RequestParam String publicId
    ) {
        cloudinaryService.deleteVideo(publicId);

        return ApiResponse.<Void>builder()
                .message("Delete audio successfully")
                .build();
    }

}
