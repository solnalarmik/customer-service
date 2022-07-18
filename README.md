# CUSTOMER-SERVICE-APPLICATION

Customer-service application is the web-application allows to get information from the user,
field values validation from user data transfer objects in response, having customer create, 
read and update functionality, to execute according user request.   
Implementing the following functionality:

POST: /customers/register\
GET: /customers/all\
GET: /customers    - filtering data by parametrs firstNameIn, lastNameIn, postCodeIn\
(for example: http://localhost:8083/customers?firstNameIn=Olya&lastNameIn=Petrova )\
GET: /customers/{id}\
PUT: /customers/{id}

## Installing
1. Install [IntelliJ IDEA Ultimate](https://www.jetbrains.com/ru-ru/idea/download)
    - fork customer-service project;
2.  Run application. 
3.  The DB connecting: http://localhost:8083/h2-console \
    Properties:\
    Driver Class: org.h2.Driver\
    JDBC URL: jdbc:h2:mem:test\
    User Name: sa \
    Password: password
    

## Technologies, tools

- Java 11
- Hibernate
- SpringBoot
- H2
- Maven
- JUnit
- Mockito
