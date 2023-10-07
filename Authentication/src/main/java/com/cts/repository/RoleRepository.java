package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.model.ERole;
import com.cts.model.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{
	
	Optional<Role> findByName(String name);

	List<Role> findByName(ERole roleCustomer);

}
