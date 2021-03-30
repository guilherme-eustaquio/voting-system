package com.softdesign.votingsystem.application.exception;

public class ThemeNotFoundException extends CustomException {
    public ThemeNotFoundException(String message, String code) {
        super(message, code);
    }
}