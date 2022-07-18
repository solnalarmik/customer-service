package com.example.customerserviceapp.controller;

import com.example.customerserviceapp.dto.CustomerRequestDto;
import com.example.customerserviceapp.dto.mapper.CustomerMapper;
import com.example.customerserviceapp.exception.BadRequestException;
import com.example.customerserviceapp.exception.NotFoundException;
import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.customerserviceapp.util.DateTimePatternUtil.DATE_TIME_PATTERN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void testRegisterWithNotExistCustomer() throws Exception {
        CustomerRequestDto customer = new CustomerRequestDto();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerDate = "15.06.2022 14:14:23";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";

        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setDate(customerDate);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);

        Customer saveCustomer = new Customer();
        saveCustomer.setFirstName(customerFirstName);
        saveCustomer.setLastName(customerLastName);
        saveCustomer.setAddress(customerAddress);
        saveCustomer.setPostCode(customerPostCode);
        saveCustomer.setDate(LocalDateTime.parse(customerDate, formatter));

        Customer customerFromDB = saveCustomer;
        customerFromDB.setId(1L);

        Mockito.when(customerService.save(customerMapper.toModel(customer))).thenReturn(customerFromDB);
        this.mockMvc.perform(post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testRegisterWithExistCustomer() throws Exception {
        CustomerRequestDto customer = new CustomerRequestDto();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";
        String customerDate = "15.06.2022 14:14:23";
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        customer.setDate(customerDate);

        Mockito.when(customerService.findByFirstNameAndLastName(customerFirstName, customerLastName))
                .thenThrow(new BadRequestException("Customer "
                        + " with first name " + customer.getFirstName()
                        + " and last name " + customer.getLastName()
                        + " already exists in DB"));

        this.mockMvc.perform(post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    public void findAll() {
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

        Mockito.when(customerService.findAll()).thenReturn(mockCustomers);

        RestAssuredMockMvc.when()
                .get("/customers/all")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(3))
                .body("[0].id", Matchers.equalTo(1))
                .body("[0].firstName", Matchers.equalTo("Olya"))
                .body("[0].lastName", Matchers.equalTo("Tkach"))
                .body("[0].date", Matchers.equalTo("15.06.2022 14:14:23"))
                .body("[0].address", Matchers.equalTo("Kyiv, Vasciv 100"))
                .body("[0].postCode", Matchers.equalTo("06785"))
                .body("[1].id", Matchers.equalTo(2))
                .body("[1].firstName", Matchers.equalTo("Tetiana"))
                .body("[1].lastName", Matchers.equalTo("Ushakova"))
                .body("[1].date", Matchers.equalTo("15.06.2022 14:14:23"))
                .body("[1].address", Matchers.equalTo("London, Vasciv 100"))
                .body("[1].postCode", Matchers.equalTo("SE8 5XA"))
                .body("[2].id", Matchers.equalTo(3))
                .body("[2].firstName", Matchers.equalTo("Ivan"))
                .body("[2].lastName", Matchers.equalTo("Petrov"))
                .body("[2].date", Matchers.equalTo("15.06.2022 14:14:23"))
                .body("[2].address", Matchers.equalTo("Kyiv, Sotni 100"))
                .body("[2].postCode", Matchers.equalTo("06785"));
    }

    @Test
    void testFindAllFilterByLastNameAndPostCode() throws Exception{
        List<Customer> customersByLastNameAndPostCode = List.of(
                new Customer(2L, "Tetiana", "Ushakova",
                        LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                        "London, Vasciv 100", "SE8 5XA"),
                new Customer(4L, "Marina", "Ushakova",
                        LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                        "London, Vasciv 100", "SE8 5XA"));
        String lastNameIn = "Ushakova";
        String postCodeIn = "SE8 5XA";
        Map<String, String> params = new HashMap<>();
        params.put("lastNameIn","Ushakova");
        params.put("postCodeIn", "SE8 5XA" );

        Mockito.when(customerService.findAll(params)).thenReturn(customersByLastNameAndPostCode);

        RestAssuredMockMvc.given()
                .queryParam("lastNameIn",lastNameIn)
                .queryParam("postCodeIn", postCodeIn)
                .get("/customers")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(2))
                .body("[0].firstName", Matchers.equalTo("Tetiana"))
                .body("[0].lastName", Matchers.equalTo("Ushakova"))
                .body("[0].date", Matchers.equalTo("15.06.2022 14:14:23"))
                .body("[0].address", Matchers.equalTo("London, Vasciv 100"))
                .body("[0].postCode", Matchers.equalTo("SE8 5XA"))
                .body("[1].id", Matchers.equalTo(4))
                .body("[1].firstName", Matchers.equalTo("Marina"))
                .body("[1].lastName", Matchers.equalTo("Ushakova"))
                .body("[1].date", Matchers.equalTo("15.06.2022 14:14:23"))
                .body("[1].address", Matchers.equalTo("London, Vasciv 100"))
                .body("[1].postCode", Matchers.equalTo("SE8 5XA"));
    }

    @Test
    void testFindByIdWithExistId() {
        Long userId = 2L;
        Optional<Customer> optionalCustomer = Optional.of(new Customer(2L, "Tetiana",
                "Ushakova", LocalDateTime.parse("15.06.2022 14:14:23", formatter),
                "London, Vasciv 100", "SE8 5XA"));

        Mockito.when(customerService.findById(userId)).thenReturn(optionalCustomer);

        RestAssuredMockMvc.when()
                .get("/customers/" + userId)
                .then()
                .statusCode(200)
                .body("firstName", Matchers.equalTo(optionalCustomer.get().getFirstName()))
                .body("lastName", Matchers.equalTo(optionalCustomer.get().getLastName()))
                .body("address", Matchers.equalTo(optionalCustomer.get().getAddress()))
                .body("postCode", Matchers.equalTo(optionalCustomer.get().getPostCode()))
                .expect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testFindByIdWithNotExistIdReturn400() throws Exception {
        Long userId = 2L;
        Optional<Customer> optionalCustomer = Optional.empty();

        Mockito.when(customerService.findById(userId)).thenReturn(optionalCustomer);
        RestAssuredMockMvc.when()
                .get("/customers/" + userId)
                .then()
                .statusCode(404)
                .expect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void testUpdateWithExistId() throws Exception {
        Customer customer = new Customer();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";
        String customerDate = "15.06.2022 14:14:23";
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        customer.setDate(LocalDateTime.parse(customerDate, formatter));

        Customer customerUpdate = new Customer();
        Long customerId = 1L;
        String customerUpdateAddress = "London";
        customerUpdate.setId(1L);
        customerUpdate.setFirstName(customerFirstName);
        customerUpdate.setLastName(customerLastName);
        customerUpdate.setAddress(customerUpdateAddress);
        customerUpdate.setPostCode(customerPostCode);
        customerUpdate.setDate(LocalDateTime.parse(customerDate, formatter));

        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setFirstName(customerFirstName);
        customerRequestDto.setLastName(customerLastName);
        customerRequestDto.setDate(customerDate);
        customerRequestDto.setAddress(customerAddress);
        customerRequestDto.setPostCode(customerPostCode);

        Mockito.when(customerService.update(customerId,customer)).thenReturn(customerUpdate);

        this.mockMvc.perform(put("/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isOk())
                .equals(customerMapper.toResponseDto(customerUpdate));
    }

    @Test
    void testUpdateWithNotExistId() throws Exception {
        Customer customer = new Customer();
        String customerFirstName = "Bob";
        String customerLastName = "Kroha";
        String customerAddress = "Kyiv";
        String customerPostCode = "03057";
        String customerDate = "15.06.2022 14:14:23";
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setAddress(customerAddress);
        customer.setPostCode(customerPostCode);
        customer.setDate(LocalDateTime.parse(customerDate, formatter));

        Customer customerUpdate = new Customer();
        Long customerId = 1L;
        String customerUpdateAddress = "London";
        customerUpdate.setId(customerId);
        customerUpdate.setFirstName(customerFirstName);
        customerUpdate.setLastName(customerLastName);
        customerUpdate.setAddress(customerUpdateAddress);
        customerUpdate.setPostCode(customerPostCode);
        customerUpdate.setDate(LocalDateTime.parse(customerDate, formatter));

        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setFirstName(customerFirstName);
        customerRequestDto.setLastName(customerLastName);
        customerRequestDto.setDate(customerDate);
        customerRequestDto.setAddress(customerAddress);
        customerRequestDto.setPostCode(customerPostCode);

        Mockito.when(customerService.update(customerId,customer))
                .thenThrow(new NotFoundException("Customer with Id " + customerId
                + " wasn't found in DB"));

        this.mockMvc.perform(put("/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isNotFound());
    }
}