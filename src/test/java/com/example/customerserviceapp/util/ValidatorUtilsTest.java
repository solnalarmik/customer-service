package com.example.customerserviceapp.util;

import com.example.customerserviceapp.exception.InfoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValidatorUtilsTest {

    @Test
    void checkFirstNameWithEmptyFirstName() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkFirstName(""),
                "Empty customer first name");
    }

    @Test
    void checkLastNameWithEmptyLastName() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkLastName(""),
                "Empty customer last name");
    }

    @Test
    void checkAddressWithEmptyAddress() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkAddress(""),
                "Empty customer address");
    }

    @Test
    void checkPostCodeWithNull() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkPostCode(null),
                "Empty customer post code");
    }

    @Test
    void checkDateWithWrongFormat() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkDate("345"),
                "Wrong format. Date format is : dd.MM.yyyy HH:mm:ss");
    }

    @Test
    void checkDateWithNull() {
        Assertions.assertThrows(InfoException.class, () -> ValidatorUtils.checkDate(null),
                "Empty customer date. Date format is : dd.MM.yyyy HH:mm:ss");
    }
}