package com.example.customerserviceapp.service;

import com.example.customerserviceapp.model.Customer;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    List<Customer> findAll(Map<String, String> param);

    Customer save(Customer customer);

    Customer update(Long id, Customer customer);

    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);
}
