package com.dmnstage.api.repositories;

import com.dmnstage.api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getByName(String name);
}
