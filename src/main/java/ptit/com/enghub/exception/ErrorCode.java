package ptit.com.enghub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Thông tin đăng nhập không hợp lệ", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(1007, "Invalid refresh token", HttpStatus.UNAUTHORIZED),
    TOPIC_NOT_EXISTED(1008, "Topic not existed", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1019, "Unauthorized access", HttpStatus.UNAUTHORIZED),
    INVALID_STATE(1021, "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST),
    VOCAB_NOT_EXISTED(1009, "Vocab not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1010, "Role not found", HttpStatus.NOT_FOUND),
    SETTINGS_NOT_FOUND(1011, "Setting not found", HttpStatus.NOT_FOUND),
    INVALID_OLD_PASSWORD(1012, "Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1013, "Mật khẩu xác nhận không khớp", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1014, "Token đã hết hạn", HttpStatus.UNAUTHORIZED);

    ErrorCode(int code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus httpStatusCode;
}