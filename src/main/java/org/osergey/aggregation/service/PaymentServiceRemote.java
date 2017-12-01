package org.osergey.aggregation.service;

import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
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
    public PaymentResponse findOne(int id) {
        return rest.getForObject(paymentOne, PaymentResponse.class, id);
    }

    @Override
    public PaymentResponse create(int id, PaymentRequest payment) {
        return rest.getForObject("http://localhost:8083" + rest.postForLocation(paymentOne, payment, id), PaymentResponse.class);
    }

    @Override
    public PaymentResponse update(int id, PaymentRequest payment) {
        return rest.patchForObject(paymentOne, payment, PaymentResponse.class, id);
    }

    @Override
    public void delete(int id) {
        rest.delete(paymentOne, id);
    }
}
