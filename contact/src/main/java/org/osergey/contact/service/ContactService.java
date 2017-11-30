package org.osergey.contact.service;

import org.osergey.contact.model.Contact;

import java.util.List;

public interface ContactService {
    Contact findOne(int id);
    Contact create(int id, Contact contact);
    Contact update(int id, Contact contact);
    void delete(int id);
}
