package org.osergey.payment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osergey.payment.model.Payment;
import org.osergey.payment.service.PaymentService;
import org.osergey.payment.web.PaymentController;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {
    private final List<Payment> payments = new ArrayList<>();

    @Before
    public void initContacts() {
        Payment payment = new Payment();
        payment.setSalary(600);
        payment.setAccount("00-12-12-12-CD");
        payments.add(payment);
    }

    @Test
    public void testFindOne() throws Exception {
        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.findOne(1)).thenReturn(payments.get(0));

        PaymentController contactController = new PaymentController();
        ReflectionTestUtils.setField(contactController, "paymentService", paymentService);

        Payment payment = contactController.findOne(1);
        assertEquals(600, payment.getSalary());
        assertEquals("00-12-12-12-CD", payment.getAccount());
    }

    @Test
    public void testCreate() throws Exception {
        final Payment newPayment = new Payment();
        newPayment.setSalary(1200);
        newPayment.setAccount("11-22-33-44-55-66-FG");

        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.create(1, newPayment)).thenAnswer(new Answer<Payment>() {
            @Override
            public Payment answer(InvocationOnMock invocation) throws Throwable {
                payments.add(newPayment);
                return newPayment;
            }
        });

        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        HttpServletResponse responce = new MockHttpServletResponse();
        paymentController.create(1, newPayment, responce);
        assertEquals("/payment/1", responce.getHeader("Location"));
        assertEquals(2, payments.size());
        assertEquals(1200, payments.get(1).getSalary());
        assertEquals("11-22-33-44-55-66-FG", payments.get(1).getAccount());
    }

    @Test
    public void testUpdate() throws Exception {
        final Payment updPayment = new Payment();
        updPayment.setSalary(890);
        updPayment.setAccount("00-00-99-00-FD");

        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.update(0, updPayment)).thenAnswer(new Answer<Payment>() {
            @Override
            public Payment answer(InvocationOnMock invocation) throws Throwable {
                Payment cur = payments.get(0);
                cur.setSalary(updPayment.getSalary());
                cur.setAccount(updPayment.getAccount());
                return cur;
            }
        });

        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        Payment ret = paymentController.update(0, updPayment);
        assertEquals(890, ret.getSalary());
        assertEquals("00-00-99-00-FD", ret.getAccount());
    }

    public void testDelete() throws  Exception {
        PaymentService paymentService = mock(PaymentService.class);
        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        paymentController.delete(0);
        verify(paymentService).delete(0);
    }
}
