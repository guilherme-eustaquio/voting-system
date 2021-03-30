package com.softdesign.votingsystem.application.exception;

public class SessionNotFoundException extends CustomException {
    public SessionNotFoundException(String message, String code) {
        super(message, code);
    }
}