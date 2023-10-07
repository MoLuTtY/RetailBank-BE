package com.cts.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("/")                  // accessible to all users irrespective of login or not  -----> NoAuth
	public String showMainPage() {
		return "Hello world!!";
	}
	
	@GetMapping("/greet")            // accessible to login users only ------> BasicAuth
	public String greetPage() {
		return "Hello from Greet page";
	}
	
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")   // accessible to admin only -------> BasicAuth
	@PostMapping("/hi")
	public String welcome() {
		return "welcome to post page";
	}

}
