package ptit.com.enghub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.RoleRequest;
import ptit.com.enghub.dto.response.RoleResponse;
import ptit.com.enghub.entity.Role;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.mapper.RoleMapper;
import ptit.com.enghub.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public List<RoleResponse> getAllRoles(){
        return roleRepository.findAll()
                .stream().map(role -> roleMapper.toRoleResonse(role))
                .collect(Collectors.toList());
    }

    public RoleResponse createRole(RoleRequest request){
        if (roleRepository.existsById(request.getName())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = roleMapper.toRole(request);
        return roleMapper.toRoleResonse(roleRepository.save(role));
    }

    public String deleteRole(String role){
        roleRepository.deleteById(role);
        return "role was deleted";
    }
}
