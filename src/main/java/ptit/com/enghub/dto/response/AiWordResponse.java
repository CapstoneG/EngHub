package ptit.com.enghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiWordResponse implements Serializable {
    private List<String> synonyms;
    private String explanation;
}

