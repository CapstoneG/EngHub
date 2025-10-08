package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

//    @GetMapping("/dashboard")
//    public ResponseEntity<ResponseDto> getDashboard() {
//        ResponseDto response = new ResponseDto(
//                "200",
//                "Welcome to Admin Dashboard",
//                Map.of(
//                        "totalUsers", 120,
//                        "activeUsers", 85
//                )
//        );
//
//        return ResponseEntity.ok(response);
//    }


}
