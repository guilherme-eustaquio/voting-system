package com.softdesign.votingsystem.application.exception;

public class SessionExpiredException extends CustomException {
    public SessionExpiredException(String message, String code) {
        super(message, code);
    }
}