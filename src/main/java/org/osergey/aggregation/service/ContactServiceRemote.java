package org.osergey.aggregation.service;

import org.osergey.contact.model.Contact;
import org.osergey.contact.service.ContactService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service("remoteContactService")
public class ContactServiceRemote implements ContactService{

    private static final String contactOne  = "http://localhost:8082/contact/{id}";

    private RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void fixPostMethod() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
    }

    @Override
    public Contact findOne(int id) {
        return rest.getForObject(contactOne, Contact.class, id);
    }

    @Override
    public Contact create(int id, Contact contact) {
        return rest.getForObject("http://localhost:8082" + rest.postForLocation(contactOne, contact, id), Contact.class);
    }

    @Override
    public Contact update(int id, Contact contact) {
        return rest.patchForObject(contactOne, contact, Contact.class, id);
    }

    @Override
    public void delete(int id) {
        rest.delete(contactOne, id);
    }
}
