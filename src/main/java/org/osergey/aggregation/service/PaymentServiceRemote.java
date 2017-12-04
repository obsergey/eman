package org.osergey.aggregation.service;

import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.service.PaymentNotFoundException;
import org.osergey.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service("remotePaymentService")
public class PaymentServiceRemote implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceRemote.class);
    private static final String paymentOne = "http://localhost:8083/payment/{id}";

    private final RestTemplate rest = new RestTemplate();
    private final BlockingQueue<Integer> queue = new LinkedBlockingDeque<>();

    private void throwNotFoundExceptionWhenNeed(HttpClientErrorException e, int id) {
        if(e.getStatusCode().value() == 404) {
            PaymentNotFoundException nex = new PaymentNotFoundException(id);
            nex.initCause(e);
            throw nex;
        }
    }

    @PostConstruct
    public void fixPostMethod() {
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Scheduled(fixedDelay = 5000)
    public void deletePaymentTask() {
        while(!queue.isEmpty()) {
            try {
                int id = queue.take();
                try {
                    rest.delete(paymentOne, id);
                    log.info("Delayed delete payment " + id + " ok");
                } catch (Exception e) {
                    log.info("Delayed delete payment " + id + " failed: " + e.toString());
                    queue.put(id);
                    return;
                }
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
    }

    @Override
    public PaymentResponse findOne(int id) {
        try {
            return rest.getForObject(paymentOne, PaymentResponse.class, id);
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
    public PaymentResponse create(int id, PaymentRequest payment) {
        try {
            return rest.getForObject("http://localhost:8083" + rest.postForLocation(paymentOne, payment, id), PaymentResponse.class);
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
    public PaymentResponse update(int id, PaymentRequest payment) {
        try {
            return rest.patchForObject(paymentOne, payment, PaymentResponse.class, id);
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
    public void delete(int id) {
        try {
            try {
                rest.delete(paymentOne, id);
                log.info("Delete payment " + id + " ok");
            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e, id);
                log.info("Delete payment " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            } catch (Exception e) {
                log.info("Delete payment " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }
}
