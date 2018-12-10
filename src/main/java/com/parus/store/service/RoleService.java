package com.parus.store.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parus.store.config.jpa.repository.RoleRepository;
import com.parus.store.model.Role;
import com.parus.store.model.RoleName;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Find role in the database by name
	 */
	public Optional<Role> findByRole(RoleName roleName) {
		return roleRepository.findByRole(roleName);
	}

	/**
	 * Find all roles from the database
	 */
	public Collection<Role> findAll() {
		return roleRepository.findAll();
	}

}
