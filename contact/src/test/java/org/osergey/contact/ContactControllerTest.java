package org.osergey.contact;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osergey.contact.model.Contact;
import org.osergey.contact.service.ContactService;
import org.osergey.contact.web.ContactController;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class ContactControllerTest {
    private final List<Contact> contacts = new ArrayList<>();

    @Before
    public void initContacts() {
        Contact contact = new Contact();
        contact.setName("Test Contact");
        contact.setPhone("00-12-12-12");
        contacts.add(contact);
    }

    @Test
    public void testFindOne() throws Exception {
        ContactService contactService = mock(ContactService.class);
        when(contactService.findOne(1)).thenReturn(contacts.get(0));

        ContactController contactController = new ContactController();
        ReflectionTestUtils.setField(contactController, "contactService", contactService);

        Contact contact = contactController.findOne(1);
        assertEquals("Test Contact", contact.getName());
        assertEquals("00-12-12-12", contact.getPhone());
    }

    @Test
    public void testCreate() throws Exception {
        final Contact newContact = new Contact();
        newContact.setName("New Contact");
        newContact.setPhone("11-22-33-44-55-66");

        ContactService contactService = mock(ContactService.class);
        when(contactService.create(1, newContact)).thenAnswer(new Answer<Contact>() {
            @Override
            public Contact answer(InvocationOnMock invocation) throws Throwable {
                contacts.add(newContact);
                return newContact;
            }
        });

        ContactController contactController = new ContactController();
        ReflectionTestUtils.setField(contactController, "contactService", contactService);

        HttpServletResponse responce = new MockHttpServletResponse();
        contactController.create(1, newContact, responce);
        assertEquals("/contact/1", responce.getHeader("Location"));
        assertEquals(2, contacts.size());
        assertEquals("New Contact", contacts.get(1).getName());
        assertEquals("11-22-33-44-55-66", contacts.get(1).getPhone());
    }

    @Test
    public void testUpdate() throws Exception {
        final Contact updContact = new Contact();
        updContact.setName("Updated Name");
        updContact.setPhone("00-00-99-00");

        ContactService contactService = mock(ContactService.class);
        when(contactService.update(0, updContact)).thenAnswer(new Answer<Contact>() {
            @Override
            public Contact answer(InvocationOnMock invocation) throws Throwable {
                Contact cur = contacts.get(0);
                cur.setName(updContact.getName());
                cur.setPhone(updContact.getPhone());
                return cur;
            }
        });

        ContactController contactController = new ContactController();
        ReflectionTestUtils.setField(contactController, "contactService", contactService);

        Contact ret = contactController.update(0, updContact);
        assertEquals("Updated Name", ret.getName());
        assertEquals("00-00-99-00", ret.getPhone());
    }

    public void testDelete() throws  Exception {
        ContactService contactService = mock(ContactService.class);
        ContactController contactController = new ContactController();
        ReflectionTestUtils.setField(contactController, "contactService", contactService);

        contactController.delete(0);
        verify(contactService).delete(0);
    }
}
