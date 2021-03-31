package com.softdesign.votingsystem.application.exception;

public class ConstraintBreakException extends CustomException {
    public ConstraintBreakException(String message, String code) {
        super(message, code);
    }
}