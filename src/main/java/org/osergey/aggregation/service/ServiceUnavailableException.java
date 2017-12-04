package org.osergey.aggregation.service;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String service) {
        super("Service " + service + " unavailable now. Please, repeat request later.");
    }
}
