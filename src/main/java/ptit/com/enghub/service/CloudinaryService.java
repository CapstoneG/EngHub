package ptit.com.enghub.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptit.com.enghub.dto.response.UploadResponse;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public UploadResponse uploadImage(MultipartFile file) {
        return upload(file, "image");
    }

    public UploadResponse uploadVideo(MultipartFile file) {
        return upload(file, "video");
    }

    private UploadResponse upload(MultipartFile file, String resourceType) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "folder", "enghub"
                    )
            );

            return new UploadResponse(
                    uploadResult.get("secure_url").toString(),
                    uploadResult.get("public_id").toString()
            );

        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void deleteVideo(String publicId) {
        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "video")
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}