package com.paguemob.exception;


import io.swagger.v3.oas.annotations.Hidden;

import java.util.Collections;
import java.util.List;

@Hidden
public class ApiErrors {

    private final List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String message) {
        this.errors = Collections.singletonList(message);
    }
}
