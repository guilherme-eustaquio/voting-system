package com.softdesign.votingsystem.application.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorCode {

    private String code;
    private String message;

    public static final ErrorCode INTERNAL_ERROR = new ErrorCode("ERROR00", "Internal error");
    public static final ErrorCode ASSOCIATED_NOT_FOUND = new ErrorCode("ERROR01", "Associated not found");
    public static final ErrorCode THEME_NOT_FOUND = new ErrorCode("ERROR02", "Theme not found");
    public static final ErrorCode SESSION_NOT_FOUND = new ErrorCode("ERROR03", "Session not found");
    public static final ErrorCode ANSWER_TYPE_NOT_FOUND = new ErrorCode("ERROR04", "Answer type not found");
    public static final ErrorCode SESSION_EXPIRED = new ErrorCode("ERROR05", "Session expired");
    public static final ErrorCode SESSION_ALREADY_ANSWERED = new ErrorCode("ERROR06", "Session already answered");
    public static final ErrorCode SESSION_TIME_INVALID = new ErrorCode("ERROR07", "Invalid time session");

}
