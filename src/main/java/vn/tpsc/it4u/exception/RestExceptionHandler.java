package vn.tpsc.it4u.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import vn.tpsc.it4u.payload.ApiResponse;
import vn.tpsc.it4u.util.ApiResponseUtils;

/**
 * RestExceptionHandler
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @Autowired
    ApiResponseUtils apiResponse;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = fieldName  + " " + error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return apiResponse.error(1500, errors, request.getLocale());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        return apiResponse.error(1501, errors, request.getLocale());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return apiResponse.error(1503, ex.getLocalizedMessage(), request.getLocale());
    }

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleJsonMappingException(JsonMappingException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getPath().toString(), ex.getLocalizedMessage());
        return apiResponse.error(1504, errors, request.getLocale());
    }
	
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleJsonParseException(JsonParseException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(
            String.format("Invalid JSON at line %s, column %s", ex.getLocation().getLineNr(), 
            ex.getLocation().getColumnNr()), ex.getLocalizedMessage());
        return apiResponse.error(1505, errors, request.getLocale());
    }
	
    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleMismatchedInputException(MismatchedInputException ex, WebRequest request) {
        return apiResponse.error(5006, ex.getLocalizedMessage(), request.getLocale());
    }
	
	/**
     * IndexOutOfBoundsException
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse indexOutOfBoundsException(Exception ex, WebRequest request) {
        return apiResponse.error(5007, ex.getLocalizedMessage(), request.getLocale());
    }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorMessage handleAllException(Exception ex, WebRequest request) {
    //     return new ErrorMessage(10000, ex.getLocalizedMessage(), null);
    // }
}