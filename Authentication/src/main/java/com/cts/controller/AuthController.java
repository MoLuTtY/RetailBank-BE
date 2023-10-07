package com.cts.controller;

import com.cts.model.ERole;
import com.cts.model.JwtAuthResponse;
import com.cts.model.LoginDto;
import com.cts.model.Role;
import com.cts.model.SignupRequest;
import com.cts.model.User;
import com.cts.repository.RoleRepository;
import com.cts.repository.UserRepository;
import com.cts.service.AuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    RoleRepository roleRepo;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){

        String token=authService.login(loginDto);
        JwtAuthResponse response=new JwtAuthResponse();
        response.setAccessToken(token);

        return ResponseEntity.ok(response);

    }

    
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest signupRequest) {
        
        List<Role> customerRoles = roleRepo.findByName(ERole.ROLE_CUSTOMER);

        Role role;
        if (!customerRoles.isEmpty()) {
            role = customerRoles.get(0);
        } else {
            
            role = new Role();
            role.setName(ERole.ROLE_CUSTOMER);
            role = roleRepo.save(role);
        }

        User user = new User();  
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepo.save(user);
    }

   
}
