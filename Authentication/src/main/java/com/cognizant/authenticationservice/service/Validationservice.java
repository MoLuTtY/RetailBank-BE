package com.cognizant.authenticationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.authenticationservice.model.AuthenticationResponse;
import com.cognizant.authenticationservice.repository.UserRepository;

@Component
public class Validationservice {

	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private UserRepository userRepo;

	public AuthenticationResponse validate(String token) {
	    AuthenticationResponse authenticationResponse = new AuthenticationResponse();

	    if (token.startsWith("Bearer ")) {
	        token = token.substring(7); 
	    }

	    System.out.println("Received token: {}"+ token);
	    if (jwtutil.validateToken(token)) {
	        String extractedUsername = jwtutil.extractUsername(token);
	       
	        System.out.println("Valid token for user: {}"+ extractedUsername);
	        authenticationResponse.setUserid(extractedUsername);
	        authenticationResponse.setValid(true);
	        authenticationResponse.setName(userRepo.findById(extractedUsername).get().getUsername());
	    } else {
	    	System.out.println("Token validation failed");
	        authenticationResponse.setValid(false);
	    }

	    return authenticationResponse;
	}


}