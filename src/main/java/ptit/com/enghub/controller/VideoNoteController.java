package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.VideoNotesDTO;
import ptit.com.enghub.service.VideoNoteService;

@RestController
@RequestMapping("/api/video-notes")
@RequiredArgsConstructor
public class VideoNoteController {

    private final VideoNoteService videoNoteService;

    @PostMapping
    public ResponseEntity<?> createNote(
            @RequestBody VideoNotesDTO request
    ) {
        return ResponseEntity.ok(
                videoNoteService.createNote(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getNotes(
            @RequestParam Long videoId
    ) {
        return ResponseEntity.ok(
                videoNoteService.getNotes(videoId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(
            @PathVariable Long id
    ) {
        videoNoteService.deleteNote(id);
        return ResponseEntity.ok("Xóa note thành công");
    }
}
