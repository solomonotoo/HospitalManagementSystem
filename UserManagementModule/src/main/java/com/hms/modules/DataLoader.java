//package com.hms.modules;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader {
//
//    @Bean
//    CommandLineRunner loadData(RoleRepository roleRepository, UserRepository userRepository) {
//        return args -> {
//            // Create roles
//            Role adminRole = new Role();
//            adminRole.setRoleName(UserRoles.ROLE_ADMIN);
//
//            Role guestRole = new Role();
//            guestRole.setRoleName(UserRoles.ROLE_USER);
//
//            roleRepository.save(adminRole);
//            roleRepository.save(guestRole);
//
//            // Create users
//            User adminUser = new User();
//            adminUser.setFirstName("John");
//            adminUser.setLastName("Doe");
//            adminUser.addRole(adminRole);
//
//            User guestUser = new User();
//            guestUser.setFirstName("Jane");
//            guestUser.setLastName("Smith");
//            guestUser.addRole(guestRole);
//
//            userRepository.save(adminUser);
//            userRepository.save(guestUser);
//
//            // Verify saved roles
//            roleRepository.findAll().forEach(role -> {
//                System.out.println("Role: " + role.getRoleName() + " - " + role.getRoleDescription());
//            });
//        };
//    }
//}
