package com.softdesign.votingsystem.application.exception;

public class SessionAlreadyAnsweredException extends CustomException {
    public SessionAlreadyAnsweredException(String message, String code) {
        super(message, code);
    }
}