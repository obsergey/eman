package org.osergey.aggregation.service;

import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.service.PaymentNotFoundException;
import org.osergey.payment.service.PaymentService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service("remotePaymentService")
public class PaymentServiceRemote implements PaymentService {

    private static final String paymentOne = "http://localhost:8083/payment/{id}";

    private final RestTemplate rest = new RestTemplate();

    private RuntimeException wrapNotFoundException(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            PaymentNotFoundException nex = new PaymentNotFoundException(id);
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
    public PaymentResponse findOne(int id) {
        try {
            return rest.getForObject(paymentOne, PaymentResponse.class, id);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public PaymentResponse create(int id, PaymentRequest payment) {
        try {
            return rest.getForObject("http://localhost:8083" + rest.postForLocation(paymentOne, payment, id), PaymentResponse.class);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public PaymentResponse update(int id, PaymentRequest payment) {
        try {
            return rest.patchForObject(paymentOne, payment, PaymentResponse.class, id);
        } catch (HttpClientErrorException e) {
            throw wrapNotFoundException(e, id);
        }
    }

    @Override
    public void delete(int id) {
        rest.delete(paymentOne, id);
    }
}
