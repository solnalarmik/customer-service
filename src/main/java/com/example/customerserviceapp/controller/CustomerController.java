package com.example.customerserviceapp.controller;

import com.example.customerserviceapp.dto.CustomerRequestDto;
import com.example.customerserviceapp.dto.CustomerResponseDto;
import com.example.customerserviceapp.dto.mapper.CustomerMapper;
import com.example.customerserviceapp.exception.BadRequestException;
import com.example.customerserviceapp.exception.NotFoundException;
import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.service.CustomerService;
import com.example.customerserviceapp.util.ValidatorUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping("/register")
    public CustomerResponseDto register(@RequestBody @Valid CustomerRequestDto requestDto) {
        ValidatorUtils.checkRequestDto(requestDto);
        Optional<Customer> customerFindByFirstNameAndLastName = customerService
                .findByFirstNameAndLastName(requestDto.getFirstName(), requestDto.getLastName());
        if (customerFindByFirstNameAndLastName.isPresent()) {
            throw new BadRequestException("Customer "
                    + " with first name " + requestDto.getFirstName()
                    + " and last name " + requestDto.getLastName()
                    + " already exists in DB");
        }
        Customer customer = customerService.save(customerMapper.toModel(requestDto));
        return customerMapper.toResponseDto(customer);
    }

    @GetMapping("/all")
    public List<CustomerResponseDto> findAll() {
        return customerService.findAll()
                .stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<CustomerResponseDto> findAll(@RequestParam Map<String, String> params) {
        return customerService.findAll(params)
                .stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerResponseDto findById(@PathVariable Long id) {
        CustomerResponseDto customerResponseDto = customerMapper
                .toResponseDto(customerService.findById(id)
                        .orElseThrow(() -> new NotFoundException("Customer with Id " + id
                                + " wasn't found in DB")));
        return customerResponseDto;
    }

    @PutMapping("/{id}")
    public CustomerResponseDto update(@PathVariable Long id,
                                      @RequestBody @Valid CustomerRequestDto customerRequestDto) {
        ValidatorUtils.checkRequestDto(customerRequestDto);
        Customer customer = customerMapper.toModel(customerRequestDto);
        Customer customerUpdate = customerService.update(id, customer);
        return customerMapper.toResponseDto(customerUpdate);
    }
}
