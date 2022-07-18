package com.example.customerserviceapp.util;

import com.example.customerserviceapp.dto.CustomerRequestDto;
import com.example.customerserviceapp.exception.ExceptionTypeEnum;
import com.example.customerserviceapp.exception.InfoException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

public class ValidatorUtils {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(DateTimePatternUtil.DATE_TIME_PATTERN);

    public static void checkRequestDto(CustomerRequestDto customerRequestDto) {
        checkFirstName(customerRequestDto.getFirstName());
        checkLastName(customerRequestDto.getLastName());
        checkDate(customerRequestDto.getDate());
        checkAddress(customerRequestDto.getAddress());
        checkPostCode(customerRequestDto.getPostCode());
    }

    public static void checkFirstName(String firstName) {
        if (StringUtils.isBlank(firstName)) {
            throw new InfoException(ExceptionTypeEnum.emptyCustomerFirstName);
        }
    }

    public static void checkLastName(String lastName) {
        if (StringUtils.isBlank(lastName)) {
            throw new InfoException(ExceptionTypeEnum.emptyCustomerLastName);
        }
    }

    public static void checkAddress(String address) {
        if (StringUtils.isBlank(address)) {
            throw new InfoException(ExceptionTypeEnum.emptyCustomerAddress);
        }
    }

    public static void checkPostCode(String postCode) {
        if (StringUtils.isBlank(postCode)) {
            throw new InfoException(ExceptionTypeEnum.emptyCustomerPostcode);
        }
    }

    public static void checkDate(String date) {
        if (StringUtils.isBlank(date)) {
            throw new InfoException(ExceptionTypeEnum.emptyCustomerDate);
        }
        try {
            LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            throw new InfoException(ExceptionTypeEnum.wrongFormatCustomerDate);
        }
    }

}
