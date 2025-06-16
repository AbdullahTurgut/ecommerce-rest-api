package com.dailyalcorcode.buynowdotcom.data;

import com.dailyalcorcode.buynowdotcom.model.Role;
import com.dailyalcorcode.buynowdotcom.model.User;
import com.dailyalcorcode.buynowdotcom.repository.RoleRepository;
import com.dailyalcorcode.buynowdotcom.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN", "ROLE_CUSTOMER");
        createDefaultRoles(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoles(Set<String> roles) {
        roles
                .stream()
                .filter(role -> Optional.ofNullable(roleRepository.findByName(role)).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }

    // burda @Transactional olması lazım ama method seviyesinde olursa hata alıcaz
    // detached olan adminRole u active state e taşımak amaçlı @Transactional
    private void createDefaultAdminIfNotExists() {
        Role adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"))
                .orElseThrow(() -> new EntityNotFoundException("Admin role not found"));

        for (int i = 1; i <= 3; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Shop user" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
        }
    }
}
