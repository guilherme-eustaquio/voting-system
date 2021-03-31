package com.softdesign.votingsystem.application.exception;

public class AnswerTypeAlreadyExistsException extends CustomException {
    public AnswerTypeAlreadyExistsException(String message, String code) {
        super(message, code);
    }
}