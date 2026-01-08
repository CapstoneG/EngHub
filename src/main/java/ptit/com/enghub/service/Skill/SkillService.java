package ptit.com.enghub.service.Skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.SkillRequest;
import ptit.com.enghub.dto.response.SkillResponse;
import ptit.com.enghub.entity.Skill;
import ptit.com.enghub.mapper.SkillMapper;
import ptit.com.enghub.repository.SkillRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final ObjectMapper objectMapper;

    public List<Skill> create(List<SkillRequest> requests) {

        List<Skill> skills = requests.stream()
                .map(this::mapToEntity)
                .toList();

        return skillRepository.saveAll(skills);
    }

    private Skill mapToEntity(SkillRequest request) {
        Skill skill = new Skill();

        skill.setTitle(request.getTitle());
        skill.setLevel(request.getLevel());
        skill.setTopic(request.getTopic());
        skill.setThumbnail(request.getThumbnail());
        skill.setSkillType(request.getSkillType());
        skill.setMediaUrl(request.getMediaUrl());

        try {
            skill.setMetadata(
                    request.getMetadata() == null
                            ? null
                            : objectMapper.writeValueAsString(request.getMetadata())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata JSON", e);
        }

        return skill;
    }

    public SkillResponse update(Long id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));

        // update các field cho phép
        skill.setTitle(request.getTitle());
        skill.setLevel(request.getLevel());
        skill.setTopic(request.getTopic());
        skill.setThumbnail(request.getThumbnail());
        skill.setSkillType(request.getSkillType());

        if (request.getMediaUrl() != null) {
            skill.setMediaUrl(request.getMediaUrl());
        }

        try {
            skill.setMetadata(
                    request.getMetadata() == null
                            ? null
                            : objectMapper.writeValueAsString(request.getMetadata())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata JSON");
        }

        Skill savedSkill = skillRepository.save(skill);
        return mapToResponse(savedSkill);
    }


    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

    public List<SkillResponse> getAll() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    public Page<SkillResponse> getBySkillType(String skillType, Pageable pageable) {

        Page<Skill> page = skillRepository
                .findBySkillType(skillType.toUpperCase(), pageable);

        return page.map(skillMapper::toResponse);
    }

    public SkillResponse getById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));

        return mapToResponse(skill);
    }

    private SkillResponse mapToResponse(Skill skill) {
        SkillResponse response = new SkillResponse();

        response.setId(skill.getId());
        response.setTitle(skill.getTitle());
        response.setLevel(skill.getLevel());
        response.setTopic(skill.getTopic());
        response.setThumbnail(skill.getThumbnail());
        response.setSkillType(skill.getSkillType());
        response.setMediaUrl(skill.getMediaUrl());

        try {
            response.setMetadata(
                    skill.getMetadata() == null
                            ? null
                            : objectMapper.readTree(skill.getMetadata())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata JSON");
        }

        return response;
    }


}
