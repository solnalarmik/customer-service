package com.example.customerserviceapp.exception;

public enum ExceptionTypeEnum {
    emptyCustomerFirstName("Empty customer first name"),
    emptyCustomerLastName("Empty customer last name"),
    emptyCustomerPostcode("Empty customer post code"),
    emptyCustomerAddress("Empty customer address"),
    emptyCustomerDate("Empty customer date. Date format is : dd.MM.yyyy HH:mm:ss"),
    wrongFormatCustomerDate("Wrong format. Date format is : dd.MM.yyyy HH:mm:ss")
    ;

    private String en;

    ExceptionTypeEnum(String en) {
        this.en = en;
    }

    public String getEn() {
        return en;
    }

}
