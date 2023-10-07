package com.cts.customer.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.customer.model.SignupRequest;


@FeignClient(name = "auth-service", url = "http://localhost:8050")
public interface AuthFeignClient {
	
	 @PostMapping("/api/auth/signup")
	 public void signup(@RequestBody SignupRequest signupRequest);

}
