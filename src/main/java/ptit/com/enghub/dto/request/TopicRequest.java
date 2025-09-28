package ptit.com.enghub.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopicRequest {
    private String name;
    private String description;
    private String level; // A1, A2, B1...
    private String imageUrl;
    private List<Long> vocabIds; // danh sách vocab gắn vào topic
}
