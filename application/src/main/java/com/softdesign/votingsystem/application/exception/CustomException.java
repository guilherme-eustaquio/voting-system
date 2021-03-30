package com.softdesign.votingsystem.application.exception;

import lombok.Data;

@Data
public abstract class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private String code;

    public CustomException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}