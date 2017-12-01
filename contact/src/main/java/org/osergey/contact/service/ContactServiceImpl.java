package org.osergey.contact.service;

import org.osergey.contact.domain.ContactEntity;
import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service("localContactService")
@ConditionalOnProperty("contact.micro.service")
@Transactional
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    @Transactional(readOnly = true)
    public ContactResponse findOne(int id) {
        return new ContactResponse(contactRepository.findOne(id));
    }

    @Override
    public ContactResponse create(int id, ContactRequest contact) {
        if(contactRepository.exists(id)) {
            throw new EntityExistsException("ContactResponse {" + id + "} already exists");
        }
        ContactEntity entity = new ContactEntity();
        entity.setId(id);
        entity.setName(contact.getName());
        entity.setPhone(contact.getPhone());
        return new ContactResponse(contactRepository.save(entity));
    }

    @Override
    public ContactResponse update(int id, ContactRequest contact) {
        ContactEntity entity = contactRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("Entity {" + id + "} not found");
        }
        entity.setName(contact.getName() != null ? contact.getName() : entity.getName());
        entity.setPhone(contact.getPhone() != null ? contact.getPhone() : entity.getPhone());
        return new ContactResponse(contactRepository.save(entity));
    }

    @Override
    public void delete(int id) {
        contactRepository.delete(id);
    }
}
