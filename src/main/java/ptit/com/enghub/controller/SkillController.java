package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.SkillRequest;
import ptit.com.enghub.dto.response.SkillResponse;
import ptit.com.enghub.entity.Skill;
import ptit.com.enghub.service.Skill.SkillService;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<List<Skill>> createSkill(
            @RequestBody List<SkillRequest> request
    ) {
        return ResponseEntity.ok(skillService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillRequest request
    ) {
        return ResponseEntity.ok(skillService.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAll());
    }

    @GetMapping("/type/{skillType}")
    public ResponseEntity<Page<SkillResponse>> getBySkillType(
            @PathVariable String skillType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                skillService.getBySkillType(skillType, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getById(id));
    }

}