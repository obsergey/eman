package org.osergey.contact.repository;

import org.osergey.contact.domain.Contact;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("contact.micro.service")
public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
