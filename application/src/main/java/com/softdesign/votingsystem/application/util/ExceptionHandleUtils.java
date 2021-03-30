package com.softdesign.votingsystem.application.util;

import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;

public class ExceptionHandleUtils {

    public static HashMap<String,String> processFieldErrors(List<FieldError> fieldErrors) {

        HashMap<String,String> message = new HashMap<>();

        for (FieldError e : fieldErrors){
            message.put(e.getField() ,e.getDefaultMessage());
        }

        return message;
    }
}
