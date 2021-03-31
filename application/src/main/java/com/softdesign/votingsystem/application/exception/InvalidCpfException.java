package com.softdesign.votingsystem.application.exception;

public class InvalidCpfException extends CustomException {
    public InvalidCpfException(String message, String code) {
        super(message, code);
    }
}