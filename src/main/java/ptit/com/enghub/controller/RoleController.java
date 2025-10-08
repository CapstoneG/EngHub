package ptit.com.enghub.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.RoleRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.service.RoleService;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleRequest request){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.createRole(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRoles(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(roleService.getAllRoles());
        return ResponseEntity.ok(apiResponse);
    }
}
