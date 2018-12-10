package com.parus.store.config.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.parus.store.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);
	Boolean existsByEmail(String Email);
	//Note both email and username are the same.It has been choosen to be same just for convenience
	Optional<User> findByEmail(String Email);

}
