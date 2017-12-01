package org.osergey.contact.service;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(int id) {
        super("Contact {" + id + "} not found");
    }
}
