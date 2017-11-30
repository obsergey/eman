package org.osergey.contact;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osergey.contact.domain.ContactEntity;
import org.osergey.contact.model.Contact;
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

    private final List<ContactEntity> contacts = new ArrayList<>();

    @Before
    public void initContacts() {
        ContactEntity contact = new ContactEntity();
        contact.setId(1);
        contact.setName("Test Contact");
        contact.setPhone("00-12-12-12");
        contacts.add(contact);
    }

    @Test
    public void testFindOne() throws Exception {
        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.findOne(1)).thenReturn(contacts.get(0));

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        Contact contact = contactService.findOne(1);
        assertEquals("Test Contact", contact.getName());
        assertEquals("00-12-12-12", contact.getPhone());
    }

    @Test
    public void testCreate() throws Exception {
        ContactEntity newContactEntity = new ContactEntity();
        newContactEntity.setId(2);
        newContactEntity.setName("New Contact");
        newContactEntity.setPhone("00-12-32-22");

        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.save(newContactEntity)).thenAnswer(new Answer<ContactEntity>() {
            @Override
            public ContactEntity answer(InvocationOnMock invocation) throws Throwable {
                newContactEntity.setId(2);
                contacts.add(newContactEntity);
                return newContactEntity;
            }
        });

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        Contact contact = contactService.create(2, new Contact(newContactEntity));
        assertEquals(2, contacts.size());
        assertEquals("New Contact", contact.getName());
        assertEquals("00-12-32-22", contact.getPhone());
    }

    @Test
    public void testUpdate() throws Exception {
        ContactEntity updContactEntity = new ContactEntity();
        updContactEntity.setId(1);
        updContactEntity.setName("Updated Contact");
        updContactEntity.setPhone("33-33-33");

        ContactRepository contactRepository = mock(ContactRepository.class);
        when(contactRepository.findOne(1)).thenReturn(contacts.get(0));
        when(contactRepository.save(updContactEntity)).thenAnswer(new Answer<ContactEntity>() {
            @Override
            public ContactEntity answer(InvocationOnMock invocation) throws Throwable {
                contacts.set(0, updContactEntity);
                return contacts.get(0);
            }
        });

        ContactService contactService = new ContactServiceImpl();
        ReflectionTestUtils.setField(contactService, "contactRepository", contactRepository);

        Contact contact = contactService.update(1, new Contact(updContactEntity));
        assertEquals("Updated Contact", contact.getName());
        assertEquals("33-33-33", contact.getPhone());
        verify(contactRepository).save(updContactEntity);
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
