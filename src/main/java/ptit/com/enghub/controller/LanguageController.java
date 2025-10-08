package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.LanguageResponse;
import ptit.com.enghub.service.IService.LanguageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping
    public ApiResponse<List<LanguageResponse>> getAllLanguages() {
        return ApiResponse.<List<LanguageResponse>>builder()
                .result(languageService.getAllLanguages())
                .build();
    }
}
