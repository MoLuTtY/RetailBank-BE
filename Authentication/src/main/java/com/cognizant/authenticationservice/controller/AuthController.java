package com.cognizant.authenticationservice.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.authenticationservice.exceptionhandling.AppUserNotFoundException;
import com.cognizant.authenticationservice.model.AppUser;
import com.cognizant.authenticationservice.model.AuthenticationResponse;
import com.cognizant.authenticationservice.model.LoginRequest;
import com.cognizant.authenticationservice.repository.UserRepository;
import com.cognizant.authenticationservice.service.CustomerDetailsService;
import com.cognizant.authenticationservice.service.LoginService;
import com.cognizant.authenticationservice.service.Validationservice;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin()
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginService loginService;

	@Autowired
	private Validationservice validationService;

	@Autowired
	private CustomerDetailsService customerService;

	@PostMapping("/login")
	public ResponseEntity<AppUser> login(@RequestBody LoginRequest appUserloginCredentials)
			throws UsernameNotFoundException, AppUserNotFoundException {
		AppUser user = loginService.userLogin(appUserloginCredentials);
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/validateToken")
	public AuthenticationResponse getValidity(@RequestHeader("Authorization") final String token) {
		
		return validationService.validate(token);
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token, @RequestBody AppUser appUserCredentials) {
		AppUser createduser = null;
		try {
			createduser = userRepository.save(appUserCredentials);
		} catch (Exception e) {
			return new ResponseEntity<String>("Not created", HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(createduser, HttpStatus.CREATED);

	}
	
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@DeleteMapping("/delete-user/{customerId}")
	public void deleteUser(@RequestHeader("Authorization") String token, @PathVariable String customerId) {
		customerService.deleteUser(token, customerId);
	}

	@GetMapping("/role/{id}")
	public String getRole(@RequestHeader("Authorization") String token, @PathVariable("id") String id) {
		return userRepository.findById(id).get().getRole();
	}

}