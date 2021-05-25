package com.epam.esm.exception;

import com.epam.esm.exception.certificate.*;
import com.epam.esm.exception.order.OrderByUserNotFoundException;
import com.epam.esm.exception.order.OrderNotFoundException;
import com.epam.esm.exception.tag.TagNameAlreadyExistsException;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.exception.user.UserLoginAlreadyExistsException;
import com.epam.esm.exception.user.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final static String ERROR_MESSAGE = "errorMessage";
    private final static String ERROR_CODE = "errorCode";

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ControllerExceptionHandler(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ResponseEntity<Object> getResponseEntity(String errorMessage,
                                                     int errorCode,
                                                     HttpStatus httpStatus) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ERROR_MESSAGE, errorMessage);
        parameters.put(ERROR_CODE, errorCode);
        return new ResponseEntity<>(parameters, httpStatus);
    }

    @ExceptionHandler({TagNameAlreadyExistsException.class})
    public ResponseEntity<Object> handleException(TagNameAlreadyExistsException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40001", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40001, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserLoginAlreadyExistsException.class})
    public ResponseEntity<Object> handleException(UserLoginAlreadyExistsException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40002", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40002, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateNameAlreadyExistsException.class})
    public ResponseEntity<Object> handleException(CertificateNameAlreadyExistsException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40003", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40003, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateIsDeletedException.class})
    public ResponseEntity<Object> handleException(CertificateIsDeletedException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40004", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40004, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnsupportedSearchParamNameCertificateException.class})
    public ResponseEntity<Object> handleException(UnsupportedSearchParamNameCertificateException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40006", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40006, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnsupportedSortedParamNameCertificateException.class})
    public ResponseEntity<Object> handleException(UnsupportedSortedParamNameCertificateException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40007", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40007, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleException(ConstraintViolationException exception,
                                                  Locale locale) {
        ConstraintViolation<?> violation = exception.getConstraintViolations().iterator().next();
        String constraintMessage = violation.getMessage();
        String constraintMessageBundle = messageSource.getMessage(constraintMessage, null, locale);
        String messageBundle =
                messageSource.getMessage("exception.message.40010", null, locale);
        return getResponseEntity(String.format(messageBundle, constraintMessageBundle),
                40010, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException exception,
                                                  Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.40011", null, locale);
        return getResponseEntity(messageBundle, 40011, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException exception,
                                                  Locale locale) {
        FieldError fieldError = exception.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        String defaultMessageBundle = messageSource.getMessage(defaultMessage, null, locale);
        String messageBundle =
                messageSource.getMessage("exception.message.40012", null, locale);
        return getResponseEntity(String.format(messageBundle, defaultMessageBundle),
                40012, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleException(BadCredentialsException exception,
                                                  Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.40101", null, locale);
        return getResponseEntity(messageBundle, 40101, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleException(AccessDeniedException exception,
                                                  Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.40301", null, locale);
        return getResponseEntity(messageBundle, 40301, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleException(ExpiredJwtException exception,
                                                  Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.40302", null, locale);
        return getResponseEntity(messageBundle, 40302, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException exception,
                                                  Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.40400", null, locale);
        return getResponseEntity(messageBundle, 40400, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TagNotFoundException.class})
    public ResponseEntity<Object> handleException(TagNotFoundException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40401", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40401, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CertificateNotFoundException.class})
    public ResponseEntity<Object> handleException(CertificateNotFoundException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40402", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40402, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleException(UserNotFoundException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40403", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40403, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<Object> handleException(OrderNotFoundException exception,
                                                  Locale locale) {
        String exceptionMessage = exception.getMessage();
        String messageBundle =
                messageSource.getMessage("exception.message.40404", null, locale);
        return getResponseEntity(String.format(messageBundle, exceptionMessage),
                40404, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderByUserNotFoundException.class)
    public ResponseEntity<Object> handleException(OrderByUserNotFoundException exception,
                                                  Locale locale) {
        int userId = exception.getUserId();
        int orderId = exception.getOrderId();
        String messageBundle =
                messageSource.getMessage("exception.message.40310", null, locale);
        return getResponseEntity(String.format(messageBundle, orderId, userId),
                40310, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleException(Locale locale) {
        String messageBundle =
                messageSource.getMessage("exception.message.50001", null, locale);
        return getResponseEntity(messageBundle, 50001, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}