package com.cts.customer.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="CUSTOMERS")
public class Customer {
    @Id
    private Long customerId;
    @NotNull(message = "Customer name cannot be null")
    private String customerName;
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dateOfBirth;
    @NotNull(message = "Pan number cannot be null")
    private String pan;
    @NotNull(message = "Address cannot be null")
    private String address;
	 
}

