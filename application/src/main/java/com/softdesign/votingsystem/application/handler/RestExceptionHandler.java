package com.softdesign.votingsystem.application.handler;

import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeNotFoundException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.SessionAlreadyAnsweredException;
import com.softdesign.votingsystem.application.exception.SessionExpiredException;
import com.softdesign.votingsystem.application.exception.SessionNotFoundException;
import com.softdesign.votingsystem.application.exception.SessionTimeInvalidException;
import com.softdesign.votingsystem.application.exception.ThemeNotFoundException;
import com.softdesign.votingsystem.application.response.ErrorResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse exception(Exception ex) {
        LOGGER.info(ErrorCode.INTERNAL_ERROR.getMessage(), ex);
        return new ErrorResponse(null, ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getMessage());
    }

    @ExceptionHandler({AssociatedNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse associatedNotFoundException(AssociatedNotFoundException ex) {
        LOGGER.info(ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.ASSOCIATED_NOT_FOUND.getCode(), ErrorCode.ASSOCIATED_NOT_FOUND.getMessage());
    }

    @ExceptionHandler({ThemeNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse themeNotFoundException(ThemeNotFoundException ex) {
        LOGGER.info(ErrorCode.THEME_NOT_FOUND.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.THEME_NOT_FOUND.getCode(), ErrorCode.THEME_NOT_FOUND.getMessage());
    }

    @ExceptionHandler({SessionNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse sessionNotFoundException(SessionNotFoundException ex) {
        LOGGER.info(ErrorCode.SESSION_NOT_FOUND.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.SESSION_NOT_FOUND.getCode(), ErrorCode.SESSION_NOT_FOUND.getMessage());
    }

    @ExceptionHandler({AnswerTypeNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse answerTypeNotFoundException(SessionNotFoundException ex) {
        LOGGER.info(ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.ANSWER_TYPE_NOT_FOUND.getCode(), ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage());
    }

    @ExceptionHandler({SessionExpiredException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse sessionExpiredException(SessionExpiredException ex) {
        LOGGER.info(ErrorCode.SESSION_EXPIRED.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.SESSION_EXPIRED.getCode(), ErrorCode.SESSION_EXPIRED.getMessage());
    }

    @ExceptionHandler({SessionAlreadyAnsweredException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse sessionAlreadyAnsweredException(SessionAlreadyAnsweredException ex) {
        LOGGER.info(ErrorCode.SESSION_ALREADY_ANSWERED.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.SESSION_ALREADY_ANSWERED.getCode(), ErrorCode.SESSION_ALREADY_ANSWERED.getMessage());
    }

    @ExceptionHandler({SessionTimeInvalidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse sessionTimeInvalidException(SessionTimeInvalidException ex) {
        LOGGER.info(ErrorCode.SESSION_TIME_INVALID.getMessage(), ex);
        return new  ErrorResponse(null, ErrorCode.SESSION_TIME_INVALID.getCode(), ErrorCode.SESSION_TIME_INVALID.getMessage());
    }
}
