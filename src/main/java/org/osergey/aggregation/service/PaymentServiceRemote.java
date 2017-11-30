package org.osergey.aggregation.service;

import org.osergey.payment.model.Payment;
import org.osergey.payment.service.PaymentService;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service("remotePaymentService")
public class PaymentServiceRemote implements PaymentService {

    private static final String paymentOne = "http://localhost:8083/payment/{id}";

    private RestTemplate rest = new RestTemplate();

    @PostConstruct
    public void fixPostMethod() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        rest.setRequestFactory(requestFactory);
    }

    @Override
    public Payment findOne(int id) {
        return rest.getForObject(paymentOne, Payment.class, id);
    }

    @Override
    public Payment create(int id, Payment payment) {
        return rest.getForObject("http://localhost:8083" + rest.postForLocation(paymentOne, payment, id), Payment.class);
    }

    @Override
    public Payment update(int id, Payment payment) {
        return rest.patchForObject(paymentOne, payment, Payment.class, id);
    }

    @Override
    public void delete(int id) {
        rest.delete(paymentOne, id);
    }
}
