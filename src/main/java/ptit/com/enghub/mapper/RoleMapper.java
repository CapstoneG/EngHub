package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import ptit.com.enghub.dto.request.RoleRequest;
import ptit.com.enghub.dto.response.RoleResponse;
import ptit.com.enghub.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResonse(Role role);
}
