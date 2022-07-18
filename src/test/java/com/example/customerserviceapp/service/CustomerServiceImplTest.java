package com.example.customerserviceapp.service;

import com.example.customerserviceapp.exception.NotFoundException;
import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.customerserviceapp.util.DateTimePatternUtil.DATE_TIME_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository customerRepository;

    @Test
    void findAll() {
        List<Customer> mockCustomers = List.of(
                new Customer(1L, "Olya", "Tkach",
                        LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                        "Kyiv, Vasciv 100", "06785"),
                new Customer(2L, "Tetiana", "Ushakova",
                        LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                        "London, Vasciv 100", "SE8 5XA"),
                new Customer(3L, "Ivan", "Petrov",
                        LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                        "Kyiv, Sotni 100", "06785"));
        Mockito.when(customerRepository.findAll()).thenReturn(mockCustomers);
        List<Customer> actual = customerService.findAll();
        assertEquals(3, actual.size());
        assertEquals(mockCustomers, actual);
    }

    @Test
    void save() {
        Customer customer = new Customer();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerDate = "15.06.2022 14:14:23";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";

        customer.setFirstName(customerFirstName);
        customer.setDate(LocalDateTime.parse(customerDate,formatter));
        customer.setLastName(customerLastName);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        Customer customerDB = customer;
        customerDB.setId(1L);
        customerDB.setDate(LocalDateTime.now());
        Mockito.when(customerRepository.save(customer)).thenReturn(customerDB);
        Customer actual = customerService.save(customer);
        String actualDate = actual.getDate().format(formatter);
        String expectedDate = LocalDateTime.now().format(formatter);

        Assertions.assertEquals(expectedDate, actualDate);
        Assertions.assertEquals(1L, actual.getId() );
    }


    @Test
    void updateWithExistId() {
        Customer customer = new Customer();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";

        customer.setId(1L);
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        Optional<Customer> optionalCustomer = Optional.of(customer);
        Mockito.when(customerRepository.findById(1L)).thenReturn(optionalCustomer);
        Customer updateCustomer = customer;
        String updateAddress = "Vinnica, Centralna 134";
        String updatePostCode = "03057";
        updateCustomer.setDate(LocalDateTime.now());
        updateCustomer.setAddress(updateAddress);
        updateCustomer.setPostCode(updatePostCode);
        Mockito.when(customerRepository.save(updateCustomer)).thenReturn(updateCustomer);
        Customer actual = customerService.update(1L, updateCustomer);
        Assertions.assertEquals(updateCustomer,actual);
        Assertions.assertEquals("03057", actual.getPostCode());
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void updateWithNotExistId(){
        Long id = 1L;

        Mockito.when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());
        Customer updateCustomer = new Customer();
        Long customerId = 1L;
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerAddress = "Vinnica, Centralna 134";
        String customerPostCode = "03057";
        updateCustomer.setId(customerId);
        updateCustomer.setFirstName(customerFirstName);
        updateCustomer.setLastName(customerLastName);
        updateCustomer.setDate(LocalDateTime.now());
        updateCustomer.setAddress(customerAddress);
        updateCustomer.setPostCode(customerPostCode);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.update(1L, updateCustomer);
        });
        assertEquals("Customer with id " + id + " not found in DB", exception.getMessage());
    }
}