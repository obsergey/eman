package org.osergey.aggregation.service;

import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.service.ContactService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service("remoteContactService")
public class ContactServiceRemote implements ContactService{

    private static final String contactOne  = "http://localhost:8082/contact/{id}";

    private final RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void fixPostMethod() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
    }

    @Override
    public ContactResponse findOne(int id) {
        return rest.getForObject(contactOne, ContactResponse.class, id);
    }

    @Override
    public ContactResponse create(int id, ContactRequest contact) {
        return rest.getForObject("http://localhost:8082" + rest.postForLocation(contactOne, contact, id), ContactResponse.class);
    }

    @Override
    public ContactResponse update(int id, ContactRequest contact) {
        return rest.patchForObject(contactOne, contact, ContactResponse.class, id);
    }

    @Override
    public void delete(int id) {
        rest.delete(contactOne, id);
    }
}
