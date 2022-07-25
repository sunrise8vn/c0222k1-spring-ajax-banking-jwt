package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "Full Name is not blank")
    private String fullName;

    @NotBlank(message = "Email is not blank")
    private String email;

    private String phone;
    private BigDecimal balance;

    @Valid
    private LocationRegionDTO locationRegion;

    public CustomerDTO(Long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }

    public Customer toCustomer() {
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegion());
    }
}
