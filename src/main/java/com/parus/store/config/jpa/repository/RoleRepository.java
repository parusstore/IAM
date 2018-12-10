package com.parus.store.config.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.parus.store.model.Role;
import com.parus.store.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRole(RoleName roleName);
}
