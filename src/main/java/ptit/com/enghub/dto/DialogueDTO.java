package ptit.com.enghub.dto.request;


import lombok.Data;

@Data
public class DialogueRequest {
    private String speaker;
    private String text;
}