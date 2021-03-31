package com.softdesign.votingsystem.application.exception;

public class AssociatedAlreadyExistsException extends CustomException {
    public AssociatedAlreadyExistsException(String message, String code) {
        super(message, code);
    }
}