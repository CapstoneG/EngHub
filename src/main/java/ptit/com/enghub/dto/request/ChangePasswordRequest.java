package ptit.com.enghub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự")
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
