package com.cts.customer.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.customer.model.AppUser;
import com.cts.customer.model.AuthenticationResponse;


@FeignClient(name = "auth-ms", url = "http://localhost:8084")
public interface AuthFeignClient {
	
	@PostMapping(value = "/createUser")
	public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token,@RequestBody AppUser appUserCredentials);
	
	@GetMapping(value = "/validateToken")
	public AuthenticationResponse getValidity(@RequestHeader("Authorization") String token);

	@GetMapping("/role/{id}")
	public String getRole(@RequestHeader("Authorization") String token, @PathVariable("id") String id);
	
	@DeleteMapping("/delete-user/{customerId}")
	public void deleteUser(@RequestHeader("Authorization") String token, @PathVariable String customerId);

}
