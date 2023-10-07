package com.cts.security;


import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.model.User;
import com.cts.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
	
	public CustomUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		User user = userRepository.findByUsername(username)
				                  .orElseThrow(()->new UsernameNotFoundException("User does not exist"));
	
		
		
//		Set<GrantedAuthority> authorities = user.getRoles()
//												.stream()
//												.map(role->new SimpleGrantedAuthority(role.getName()))
//												.collect(Collectors.toSet());
		
		Set<GrantedAuthority> authorities = user.getRoles()
		        .stream()
		        .map(role -> new SimpleGrantedAuthority(role.getName().name())) 
		        .collect(Collectors.toSet());
				
	    UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
		
	    return userDetails;
	}

}
