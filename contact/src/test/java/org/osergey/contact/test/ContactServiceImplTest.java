package org.osergey.contact.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.osergey.contact.domain.Contact;
import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.repository.ContactRepository;
import org.osergey.contact.service.ContactService;
import org.osergey.contact.service.ContactServiceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContactServiceImplTest {

    private final List<Contact> contacts = new ArrayList<>();

    @Before
    public void initContacts() {
        Contact contact = new Contact();
        contact.setId(1);
        contact.setName("Test ContactResponse");
        contact.setPhone("00-12-12-12");
        contacts.add(contact);
    }

    @Test
    public void testFindOne() throws Exception {
        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.findOne(1)).thenReturn(contacts.get(0));

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        ContactResponse contact = contactService.findOne(1);
        assertEquals("Test ContactResponse", contact.getName());
        assertEquals("00-12-12-12", contact.getPhone());
    }

    @Test
    public void testCreate() throws Exception {
        Contact newContact = new Contact();
        newContact.setId(2);
        newContact.setName("New ContactResponse");
        newContact.setPhone("00-12-32-22");

        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.save(newContact)).thenAnswer((Answer<Contact>) invocation -> {
            newContact.setId(2);
            contacts.add(newContact);
            return newContact;
        });

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName("New ContactResponse");
        contactRequest.setPhone("00-12-32-22");

        ContactResponse contact = contactService.create(2, contactRequest);
        assertEquals(2, contacts.size());
        assertEquals("New ContactResponse", contact.getName());
        assertEquals("00-12-32-22", contact.getPhone());
    }

    @Test
    public void testUpdate() throws Exception {
        Contact updContact = new Contact();
        updContact.setId(1);
        updContact.setName("Updated ContactResponse");
        updContact.setPhone("33-33-33");

        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.findOne(1)).thenReturn(contacts.get(0));
        when(contactRepository.save(updContact)).thenAnswer((Answer<Contact>) invocation -> {
            contacts.set(0, updContact);
            return contacts.get(0);
        });

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName("Updated ContactResponse");
        contactRequest.setPhone("33-33-33");

        ContactResponse contact = contactService.update(1, contactRequest);
        assertEquals("Updated ContactResponse", contact.getName());
        assertEquals("33-33-33", contact.getPhone());
        verify(contactRepository).save(updContact);
    }

    @Test
    public void testDelete()  throws Exception {
        ContactRepository contactRepository = mock(ContactRepository.class);
        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        contactService.delete(1);
        verify(contactRepository).delete(1);
    }
}
