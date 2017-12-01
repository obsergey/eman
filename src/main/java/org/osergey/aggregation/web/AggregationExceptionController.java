package org.osergey.aggregation.web;

import org.osergey.aggregation.service.PaginationBadArgumentsException;
import org.osergey.dept.model.DeptErrorResponse;
import org.osergey.dept.service.DeptNotFoundException;
import org.osergey.dept.web.DeptExceptionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class AggregationExceptionController {
    private static final Logger log = LoggerFactory.getLogger(DeptExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DeptNotFoundException.class)
    public DeptErrorResponse notFoundDept(DeptNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new DeptErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            PaginationBadArgumentsException.class})
    public DeptErrorResponse badRequest(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new DeptErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public DeptErrorResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new DeptErrorResponse(exception.getMessage());
    }
}
