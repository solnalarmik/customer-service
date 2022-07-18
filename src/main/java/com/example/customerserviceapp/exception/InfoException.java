package com.example.customerserviceapp.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InfoException extends RuntimeException {
    protected ExceptionTypeEnum type;
    protected String exceptionMessage;

    public InfoException(ExceptionTypeEnum type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        String message;
        message = type.getEn();
        if (StringUtils.isNotEmpty(exceptionMessage)) {
            message = message.concat(" MESSAGE: ")
                    .concat(exceptionMessage);
        }
        return message;
    }
}
