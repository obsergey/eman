package org.osergey.aggregation.service;

import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.service.ContactNotFoundException;
import org.osergey.contact.service.ContactService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service("remoteContactService")
public class ContactServiceRemote implements ContactService{

    private static final String contactOne  = "http://localhost:8082/contact/{id}";

    private final RestTemplate rest = new RestTemplate();

    private RuntimeException wrapNotFoundException(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            ContactNotFoundException nex = new ContactNotFoundException(id);
            nex.initCause(e);
            return nex;
        }
        return e;
    }

    @PostConstruct
    public void fixPostMethod() {
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    public ContactResponse findOne(int id) {
        try {
            return rest.getForObject(contactOne, ContactResponse.class, id);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public ContactResponse create(int id, ContactRequest contact) {
        try {
            return rest.getForObject("http://localhost:8082" + rest.postForLocation(contactOne, contact, id), ContactResponse.class);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public ContactResponse update(int id, ContactRequest contact) {
        try {
            return rest.patchForObject(contactOne, contact, ContactResponse.class, id);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public void delete(int id) {
        rest.delete(contactOne, id);
    }
}
