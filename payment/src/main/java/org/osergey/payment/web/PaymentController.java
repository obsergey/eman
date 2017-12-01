package org.osergey.payment.web;

import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@ConditionalOnProperty("payment.micro.service")
@RequestMapping("/payment")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    @Qualifier("localPaymentService")
    PaymentService paymentService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public PaymentResponse findOne(@PathVariable int id) {
        log.info("GET /payment/" + id);
        return paymentService.findOne(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public void create(@PathVariable int id, @RequestBody PaymentRequest payment, HttpServletResponse response) {
        log.info("POST /payment/" + id + " payment " + payment);
        paymentService.create(id, payment);
        response.addHeader(HttpHeaders.LOCATION, "/payment/" + id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public PaymentResponse update(@PathVariable int id, @RequestBody PaymentRequest payment) {
        log.info("PATCH /payment/" + id + " payment " + payment);
        return paymentService.update(id, payment);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable int id) {
        log.info("DELETE /payment/" + id);
        paymentService.delete(id);
    }
}
