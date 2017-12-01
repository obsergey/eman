package org.osergey.dept.web;

import org.osergey.dept.model.DeptErrorResponse;
import org.osergey.dept.service.DeptNotFoundException;
import org.osergey.dept.service.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty("dept.micro.service")
public class DeptExceptionController {
    private static final Logger log = LoggerFactory.getLogger(DeptExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DeptNotFoundException.class)
    public DeptErrorResponse notFoundDept(DeptNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new DeptErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public DeptErrorResponse notFoundEmployee(EmployeeNotFoundException exception) {
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
