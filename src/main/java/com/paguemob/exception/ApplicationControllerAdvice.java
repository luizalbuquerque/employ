package com.paguemob.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ApplicationControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErros(MethodArgumentNotValidException e, HttpServletRequest request) {
        logToConsole(e, request);
        BindingResult bindingResult = e.getBindingResult();
        List<String> messages = bindingResult.getAllErrors()
                .stream()
                .map(m -> getResourceMessage(m.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ApiErrors(messages);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException e, HttpServletRequest request) {
        logToConsole(e, request);
        ApiErrors apiErrors = new ApiErrors(e.getReason());
        return new ResponseEntity<>(apiErrors, e.getStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrors handleException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        logToConsole(e, request);
        return new ApiErrors(getResourceMessage(e.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiErrors handleException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        logToConsole(e, request);
        return new ApiErrors(getResourceMessage(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleException(HttpMessageNotReadableException e, HttpServletRequest request) {
        logToConsole(e, request);
        return new ApiErrors(getResourceMessage("request.validation.messageNotReadable"));
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleException(BusinessException e, HttpServletRequest request) {
        logToConsole(e, request);
        return new ApiErrors(getResourceMessage(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrors handleException(Exception e, HttpServletRequest request) {
        logToConsole(e, request);
        return new ApiErrors(getResourceMessage("request.validation.internalError"));
    }

    private String getResourceMessage(String message) {
        if (ObjectUtils.isEmpty(message))
            return message;

        if (message.startsWith("{") && message.endsWith("}")) {
            message = message.replace("{", "").replace("}", "");
        }

        try {
            message = ResourceBundle.getBundle("messages").getString(message);
        } catch (MissingResourceException ignored) {
            // ignored
        }

        return message;
    }

    private void logToConsole(Exception e, HttpServletRequest request) {
        if (logger.isErrorEnabled()) {
            StringBuilder error = new StringBuilder("An error has ocurred:\n");
            error.append("Exception message: ").append(getResourceMessage(e.getMessage())).append("\n");

            if (request != null) {
                error.append("Request URI: ").append(request.getRequestURI()).append("\n");
                error.append("Request method: ").append(request.getMethod()).append("\n");
            }

            logger.error(error.toString());
        }
    }
}
