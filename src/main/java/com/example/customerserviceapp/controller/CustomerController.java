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

    @GetMapping("/inject")
    public void inject() {
        Customer customer = new Customer();
        String firstName = "Yuliia";
        String lastName = "Shevchenko";
        String address = "London";
        String postCode = "SE48XA";
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPostCode(postCode);
        customer.setAddress(address);
        customerService.save(customer);
        Customer customer1 = new Customer();
        String firstName1 = "Olya";
        String lastName1 = "Petrova";
        String postCode1 = "03057";
        String address1 = "Ukraine, Kyiv";
        customer1.setFirstName(firstName1);
        customer1.setLastName(lastName1);
        customer1.setPostCode(postCode1);
        customer1.setAddress(address1);
        Customer customer2 = new Customer();
        String firstName2 = "Masha";
        String lastName2 = "Shevchenko";
        String address2 = "Ukraine, Kyiv";
        String postCode2 = "03057";
        customer2.setFirstName(firstName2);
        customer2.setLastName(lastName2);
        customer2.setAddress(address2);
        customer2.setPostCode(postCode2);
        Customer customer3 = new Customer();
        String firstName3 = "Elena";
        String lastName3 = "Petrova";
        String address3 = "London";
        String postCode3 = "SE48XA";
        customer3.setFirstName(firstName3);
        customer3.setLastName(lastName3);
        customer3.setPostCode(postCode3);
        customer3.setAddress(address3);
        Customer customer4 = new Customer();
        String firstName4 = "Ivan";
        String address4 = "London";
        String postCode4 = "SE48XA";
        String lastName4 = lastName2;
        customer4.setFirstName(firstName4);
        customer4.setLastName(lastName4);
        customer4.setPostCode(postCode4);
        customer4.setAddress(address4);
        customerService.save(customer);
        customerService.save(customer1);
        customerService.save(customer2);
        customerService.save(customer3);
        customerService.save(customer4);
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
