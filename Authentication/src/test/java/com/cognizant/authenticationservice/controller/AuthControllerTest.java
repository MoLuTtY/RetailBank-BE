package com.cognizant.authenticationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.authenticationservice.service.CustomerDetailsService;
import com.cognizant.authenticationservice.service.LoginService;
import com.cognizant.authenticationservice.service.Validationservice;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private LoginService loginService;

	@MockBean
	private Validationservice validationService;

	@MockBean
	private CustomerDetailsService customerService;

}
