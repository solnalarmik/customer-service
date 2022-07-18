package com.example.customerserviceapp.dto;

import lombok.Data;

@Data
public class CustomerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String date;
    private String address;
    private String postCode;
}
