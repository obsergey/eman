package org.osergey.contact.service;

public class ContactExistsException extends RuntimeException {
    public ContactExistsException(int id) {
        super("Contact {" + id + "} already exists");
    }
}
