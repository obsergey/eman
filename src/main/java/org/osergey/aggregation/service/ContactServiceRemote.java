package org.osergey.aggregation.service;

import org.osergey.contact.model.ContactResponse;
import org.osergey.contact.model.ContactRequest;
import org.osergey.contact.service.ContactNotFoundException;
import org.osergey.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service("remoteContactService")
public class ContactServiceRemote implements ContactService{

    private static final Logger log = LoggerFactory.getLogger(ContactServiceRemote.class);
    private static final String contactOne  = "http://localhost:8082/contact/{id}";

    private final RestTemplate rest = new RestTemplate();
    private final BlockingQueue<Integer> queue = new LinkedBlockingDeque<>();

    private void throwNotFoundExceptionWhenNeed(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            ContactNotFoundException nex = new ContactNotFoundException(id);
            nex.initCause(e);
            throw nex;
        }
    }

    @PostConstruct
    public void fixPostMethod() {
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Scheduled(fixedDelay = 5000)
    public void deleteContactTask() {
        while(!queue.isEmpty()) {
            try {
                int id = queue.take();
                try {
                    rest.delete(contactOne, id);
                    log.info("Delayed delete contact " + id + " ok");
                } catch (Exception e) {
                    log.info("Delayed delete contact " + id + " failed: " + e.toString());
                    queue.put(id);
                    return;
                }
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
    }

    @Override
    public ContactResponse findOne(int id) {
        try {
            return rest.getForObject(contactOne, ContactResponse.class, id);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }

    @Override
    public ContactResponse create(int id, ContactRequest contact) {
        try {
            return rest.getForObject("http://localhost:8082" + rest.postForLocation(contactOne, contact, id), ContactResponse.class);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return  null;
        }
    }

    @Override
    public ContactResponse update(int id, ContactRequest contact) {
        try {
            return rest.patchForObject(contactOne, contact, ContactResponse.class, id);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return  null;
        }
    }

    @Override
    public void delete(int id) {
        try {
            try {
                rest.delete(contactOne, id);
                log.info("Delete contact " + id + " ok");
            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e, id);
                log.info("Delete contact " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            } catch (Exception e) {
                log.info("Delete contact " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }
}
