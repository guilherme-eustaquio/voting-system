package com.softdesign.votingsystem.application.exception;

public class AssociatedNotFoundException extends CustomException {
    public AssociatedNotFoundException(String message, String code) {
        super(message, code);
    }
}