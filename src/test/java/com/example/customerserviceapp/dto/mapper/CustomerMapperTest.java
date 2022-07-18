package com.example.customerserviceapp.dto.mapper;

import com.example.customerserviceapp.dto.CustomerRequestDto;
import com.example.customerserviceapp.dto.CustomerResponseDto;
import com.example.customerserviceapp.model.Customer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.customerserviceapp.util.DateTimePatternUtil.DATE_TIME_PATTERN;

@SpringBootTest
class CustomerMapperTest {
    private  static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void toModel() {
        CustomerRequestDto dto = new CustomerRequestDto();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerDate = "15.06.2022 14:14:23";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";

        dto.setFirstName(customerFirstName);
        dto.setLastName(customerLastName);
        dto.setDate(customerDate);
        dto.setAddress(customerAddress);
        dto.setPostCode(customerPostCode);

        Customer actual = customerMapper.toModel(dto);
        Customer expected = new Customer();
        expected.setFirstName(customerFirstName);
        expected.setLastName(customerLastName);
        expected.setDate(LocalDateTime.parse(customerDate, formatter));
        expected.setAddress(customerAddress);
        expected.setPostCode(customerPostCode);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void toResponseDto() {
        Customer customer = new Customer();
        Long customerId = 1L;
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerDate = "15.06.2022 14:14:23";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";

        customer.setId(customerId);
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setDate(LocalDateTime.parse(customerDate, formatter));
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        CustomerResponseDto expected = new CustomerResponseDto();
        expected.setId(1L);
        expected.setFirstName(customerFirstName);
        expected.setLastName(customerLastName);
        expected.setDate(customerDate);
        expected.setAddress(customerAddress);
        expected.setPostCode(customerPostCode);
        CustomerResponseDto actual = customerMapper.toResponseDto(customer);
        Assertions.assertEquals(expected, actual);
    }
}