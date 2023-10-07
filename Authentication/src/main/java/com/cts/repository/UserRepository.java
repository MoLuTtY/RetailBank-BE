package com.cts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	Optional<User> findByUsername(String username);
//	Optional<User> findByUsername(String username,String email);
//	Optional<User> findByEmail(String email);
}
