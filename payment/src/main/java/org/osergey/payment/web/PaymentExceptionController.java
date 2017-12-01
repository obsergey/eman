package org.osergey.payment.web;

import org.osergey.payment.model.PaymentErrorResponse;
import org.osergey.payment.service.PaymentExistsException;
import org.osergey.payment.service.PaymentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty("payment.micro.service")
public class PaymentExceptionController {
    private static final Logger log = LoggerFactory.getLogger(PaymentExceptionController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentExistsException.class)
    public PaymentErrorResponse exists(PaymentExistsException exception) {
        log.error(exception.getMessage(), exception);
        return new PaymentErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PaymentNotFoundException.class)
    public PaymentErrorResponse notFound(PaymentNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new PaymentErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public PaymentErrorResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new PaymentErrorResponse(exception.getMessage());
    }
}
