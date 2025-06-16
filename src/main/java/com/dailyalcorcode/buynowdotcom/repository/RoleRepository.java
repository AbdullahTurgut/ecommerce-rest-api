package com.dailyalcorcode.buynowdotcom.repository;

import com.dailyalcorcode.buynowdotcom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleUser);
}
