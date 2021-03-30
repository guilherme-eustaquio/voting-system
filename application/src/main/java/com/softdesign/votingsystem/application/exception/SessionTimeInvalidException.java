package com.softdesign.votingsystem.application.exception;

public class SessionTimeInvalidException extends CustomException {
    public SessionTimeInvalidException(String message, String code) {
        super(message, code);
    }
}