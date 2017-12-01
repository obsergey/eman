package org.osergey.contact.service;

import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;

public interface ContactService {
    ContactResponse findOne(int id);
    ContactResponse create(int id, ContactRequest contact);
    ContactResponse update(int id, ContactRequest contact);
    void delete(int id);
}
