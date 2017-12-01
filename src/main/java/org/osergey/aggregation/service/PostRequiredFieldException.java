package org.osergey.aggregation.service;

public class PostRequiredFieldException extends RuntimeException {
    public PostRequiredFieldException(String field) {
        super("Post method required field " + field + " not found");
    }
}
