//package ptit.com.enghub.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import ptit.com.enghub.entity.Role;
//import ptit.com.enghub.entity.User;
//import ptit.com.enghub.enums.EnumRole;
//import ptit.com.enghub.exception.AppException;
//import ptit.com.enghub.exception.ErrorCode;
//import ptit.com.enghub.repository.RoleRepository;
//import ptit.com.enghub.repository.UserRepository;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class ApplicationInitConfig {
//    private final PasswordEncoder passwordEncoder;
//    private final RoleRepository roleRepository;
//
//    @Bean
//    ApplicationRunner applicationRunner(UserRepository userRepository){
//        return args -> {
//            createRoleIfNotExists(EnumRole.ADMIN);
//            createRoleIfNotExists(EnumRole.USER);
//
////            if (userRepository.findByEmail("admin@gmail.com").isEmpty()){
////                var adminRole = roleRepository.findByName(EnumRole.ADMIN.name())
////                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
////
////                User user = User.builder()
////                        .email("admin@gmail.com")
////                        .password(passwordEncoder.encode("12345678"))
////                        .firstName("Admin")
////                        .lastName("System")
////                        .isVerified(true)
////                        .build();
////
////                user.getRoles().add(adminRole);
////                //adminRole.getUsers().add(user);
////
////
////                userRepository.save(user);
////                log.warn("✅ Admin user created: admin@gmail.com / 12345678");
////            }
//        };
//    }
//
//    private void createRoleIfNotExists(EnumRole roleName) {
//        if (!roleRepository.existsById(roleName.name())) {
//            Role role = new Role();
//            role.setName(roleName);
//            roleRepository.save(role);
//            log.info("Created role: {}", roleName);
//        }
//    }
//}
