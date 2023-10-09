package com.cts.customer.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appuser")
public class AppUser {
	
	@Id
	@Column(name = "userid", length = 20)
	@NotNull
	private String userid;
	
	@Column(name = "username", length = 20)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	private String authToken;
	
	private String role;
}