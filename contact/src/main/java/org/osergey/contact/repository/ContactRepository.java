package org.osergey.contact.repository;

import org.osergey.contact.domain.ContactEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty("contact.micro.service")
public interface ContactRepository extends CrudRepository<ContactEntity, Integer> {
}
