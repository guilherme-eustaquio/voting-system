package com.softdesign.votingsystem.application.exception;

public class AnswerTypeNotFoundException extends CustomException {
    public AnswerTypeNotFoundException(String message, String code) {
        super(message, code);
    }
}