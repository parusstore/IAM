package com.parus.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.parus.store.config.jpa.repository.UserRepository;
import com.parus.store.model.CustomUserPrincipal;
import com.parus.store.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

		@Autowired
	    private UserRepository userRepository;
	 
	    @Override
	    public UserDetails loadUserByUsername(String username) {
	        User user = userRepository.findByUsername(username);
	        return new CustomUserPrincipal(user);
	    }
}
