package org.osergey.aggregation.service;

public class PaginationBadArgumentsException extends RuntimeException{
    PaginationBadArgumentsException() {
        super("Pagination page should be positive or null, size should be positive");
    }
}
