package org.osergey.payment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.osergey.payment.domain.Payment;
import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.repository.PaymentRepository;
import org.osergey.payment.service.PaymentService;
import org.osergey.payment.service.PaymentServiceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentServiceImplTest {

    private final List<Payment> payments = new ArrayList<>();

    @Before
    public void initContacts() {
        Payment payment = new Payment();
        payment.setId(1);
        payment.setSalary(800);
        payment.setAccount("00-12-12-12-WQ");
        payments.add(payment);
    }

    @Test
    public void testFindOne() throws Exception {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        when(paymentRepository.findOne(1)).thenReturn(payments.get(0));

        PaymentService paymentService = new PaymentServiceImpl();
        ReflectionTestUtils.setField(paymentService, "paymentRepository", paymentRepository);

        PaymentResponse payment = paymentService.findOne(1);
        assertEquals(800, payment.getSalary());
        assertEquals("00-12-12-12-WQ", payment.getAccount());
    }

    @Test
    public void testCreate() throws Exception {
        Payment paymentEntity = new Payment();
        paymentEntity.setId(2);
        paymentEntity.setSalary(1200);
        paymentEntity.setAccount("00-12-32-22-HG");

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        when(paymentRepository.save(paymentEntity)).thenAnswer((Answer<Payment>) invocation -> {
            paymentEntity.setId(2);
            payments.add(paymentEntity);
            return paymentEntity;
        });

        PaymentService paymentService = new PaymentServiceImpl();
        ReflectionTestUtils.setField(paymentService, "paymentRepository", paymentRepository);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSalary(1200);
        paymentRequest.setAccount("00-12-32-22-HG");

        PaymentResponse payment = paymentService.create(2, paymentRequest);
        assertEquals(2, payments.size());
        assertEquals(1200, payment.getSalary());
        assertEquals("00-12-32-22-HG", payment.getAccount());
    }

    @Test
    public void testUpdate() throws Exception {
        Payment payment = new Payment();
        payment.setId(1);
        payment.setSalary(1800);
        payment.setAccount("33-33-33-FR");

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        when(paymentRepository.findOne(1)).thenReturn(payments.get(0));
        when(paymentRepository.save(payment)).thenAnswer((Answer<Payment>) invocation -> {
            payments.set(0, payment);
            return payments.get(0);
        });

        PaymentService paymentService = new PaymentServiceImpl();
        ReflectionTestUtils.setField(paymentService, "paymentRepository", paymentRepository);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSalary(1800);
        paymentRequest.setAccount("33-33-33-FR");

        PaymentResponse contact = paymentService.update(1, paymentRequest);
        assertEquals(1800, contact.getSalary());
        assertEquals("33-33-33-FR", contact.getAccount());
        verify(paymentRepository).save(payment);
    }

    @Test
    public void testDelete()  throws Exception {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentServiceImpl();
        ReflectionTestUtils.setField(paymentService, "paymentRepository", paymentRepository);

        paymentService.delete(1);
        verify(paymentRepository).delete(1);
    }
}
