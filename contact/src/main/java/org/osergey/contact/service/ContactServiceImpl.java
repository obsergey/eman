package org.osergey.contact.service;

import org.osergey.contact.domain.ContactEntity;
import org.osergey.contact.model.Contact;
import org.osergey.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service("localContactService")
@ConditionalOnProperty("contact.micro.service")
@Transactional
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    @Transactional(readOnly = true)
    public Contact findOne(int id) {
        return new Contact(contactRepository.findOne(id));
    }

    @Override
    public Contact create(int id, Contact contact) {
        if(contactRepository.exists(id)) {
            throw new EntityExistsException("Contact {" + id + "} already exists");
        }
        ContactEntity entity = new ContactEntity();
        entity.setId(id);
        entity.setName(contact.getName());
        entity.setPhone(contact.getPhone());
        return new Contact(contactRepository.save(entity));
    }

    @Override
    public Contact update(int id, Contact contact) {
        ContactEntity entity = contactRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("Entity {" + id + "} not found");
        }
        entity.setName(contact.getName() != null ? contact.getName() : entity.getName());
        entity.setPhone(contact.getPhone() != null ? contact.getPhone() : entity.getPhone());
        return new Contact(contactRepository.save(entity));
    }

    @Override
    public void delete(int id) {
        contactRepository.delete(id);
    }
}
