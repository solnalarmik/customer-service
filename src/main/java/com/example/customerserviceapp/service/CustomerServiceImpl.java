package com.example.customerserviceapp.service;

import com.example.customerserviceapp.exception.NotFoundException;
import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.repository.CustomerRepository;
import com.example.customerserviceapp.repository.specification.CustomerSpecificationManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerSpecificationManager customerSpecificationManager;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerSpecificationManager customerSpecificationManager) {
        this.customerRepository = customerRepository;
        this.customerSpecificationManager = customerSpecificationManager;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAll(Map<String, String> params) {
        Specification<Customer> specification = null;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Specification<Customer> sp = customerSpecificationManager.get(entry.getKey(),
                    entry.getValue().split(","));
            specification = specification == null
                    ? Specification.where(sp) : specification.and(sp);
        }
        return customerRepository.findAll(specification);
    }

    @Override
    public Customer save(Customer customer) {
        customer.setDate(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) throws NotFoundException {
        customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id
                        + " not found in DB") {
                });
        customer.setId(id);
        customer.setDate(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
