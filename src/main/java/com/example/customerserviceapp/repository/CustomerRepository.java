package com.example.customerserviceapp.repository;

import com.example.customerserviceapp.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);
}
