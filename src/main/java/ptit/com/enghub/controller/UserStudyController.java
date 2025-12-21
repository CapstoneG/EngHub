package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.EndStudyDto;
import ptit.com.enghub.dto.request.StartStudyRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.service.dashboard.StudyTrackingService;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class UserStudyController {

    private final StudyTrackingService studyTrackingService;

    @PostMapping("/start")
    public ResponseEntity<EndStudyDto> startStudy(
            @RequestBody StartStudyRequest request
    ) {
        EndStudyDto sessionResponse = studyTrackingService.startStudy(request);
        return ResponseEntity.ok(sessionResponse);
    }

    @PostMapping("/end")
    public ResponseEntity<ApiResponse<Void>> endStudy(
            @RequestBody EndStudyDto request
    ) {
        studyTrackingService.endStudy(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(0)
                        .message("End session")
                        .build()
        );
    }
}
