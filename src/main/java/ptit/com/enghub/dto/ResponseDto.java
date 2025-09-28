package ptit.com.enghub.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {
    private String status;
    private String message;
    private Object data;
}
