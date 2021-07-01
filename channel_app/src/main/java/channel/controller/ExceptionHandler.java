package channel.controller;

import channel.util.ApiException;
import common.model.ApiError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError HandleNoSuchElementException(ApiException exception, HttpServletRequest request) {
        return new ApiError(404,exception.getMessage(),request.getServletPath());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError HandleSuchElementException(ConstraintViolationException exception,HttpServletRequest request) {
        return new ApiError(404,"The entry is repeated",request.getServletPath());
    }
}
