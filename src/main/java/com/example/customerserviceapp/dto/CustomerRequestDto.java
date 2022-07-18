package com.example.customerserviceapp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    @NotNull
    private String lastName;
    private String date;
    @NotNull
    private String address;
    @NotNull
    private String postCode;

    public CustomerRequestDto() {
    }
}
