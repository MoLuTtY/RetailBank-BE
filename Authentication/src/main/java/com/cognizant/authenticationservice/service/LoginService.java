package com.cognizant.authenticationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cognizant.authenticationservice.exceptionhandling.AppUserNotFoundException;
import com.cognizant.authenticationservice.model.AppUser;
import com.cognizant.authenticationservice.model.LoginRequest;
import com.cognizant.authenticationservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Component
public class LoginService {

	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private CustomerDetailsService customerDetailservice;

	@Autowired
	private UserRepository userRepo;
	
	public AppUser userLogin(LoginRequest appuser) throws AppUserNotFoundException {
		final UserDetails userdetails = customerDetailservice.loadUserByUsername(appuser.getUsername());
		String userid = "";
		String role="";
		String token = "";
		
		AppUser user = null;
		user = userRepo.findById(appuser.getUsername()).orElse(null); 
		
		
		String userRole = userRepo.findById(appuser.getUsername()).get().getRole();

		if (userdetails.getPassword().equals(appuser.getPassword()) && userRole.equals(user.getRole()) ) {
			userid = appuser.getUsername();
			token = jwtutil.generateToken(userdetails);
			role = userRole;
			return new AppUser(userid, null, null, token,role);
		} else {
			throw new AppUserNotFoundException("Username/Password is incorrect...Please check");
		}
	}
}