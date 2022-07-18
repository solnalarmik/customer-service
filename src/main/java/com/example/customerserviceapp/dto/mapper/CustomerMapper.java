package com.example.customerserviceapp.dto.mapper;

import com.example.customerserviceapp.dto.CustomerRequestDto;
import com.example.customerserviceapp.dto.CustomerResponseDto;
import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.util.DateTimePatternUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(DateTimePatternUtil.DATE_TIME_PATTERN);

    public Customer toModel(CustomerRequestDto requestDto) {
        Customer customer = new Customer();
        customer.setFirstName(requestDto.getFirstName());
        customer.setLastName(requestDto.getLastName());
        customer.setDate(LocalDateTime.parse(requestDto.getDate(), formatter));
        customer.setAddress(requestDto.getAddress());
        customer.setPostCode(requestDto.getPostCode());
        return customer;
    }

    public CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setId(customer.getId());
        responseDto.setFirstName(customer.getFirstName());
        responseDto.setLastName(customer.getLastName());
        responseDto.setDate(customer.getDate().format(formatter));
        responseDto.setAddress(customer.getAddress());
        responseDto.setPostCode(customer.getPostCode());
        return responseDto;
    }
}
