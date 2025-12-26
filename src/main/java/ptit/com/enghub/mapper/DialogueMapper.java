package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.DialogueDTO;
import ptit.com.enghub.entity.Dialogue;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DialogueMapper {
    DialogueDTO toResponse(Dialogue dialogue);
    List<DialogueDTO> toResponses(List<Dialogue> dialogues);
}