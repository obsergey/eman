package org.osergey.payment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
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
    private final List<PaymentResponse> payments = new ArrayList<>();

    @Before
    public void initContacts() {
        PaymentResponse payment = new PaymentResponse();
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

        PaymentResponse payment = contactController.findOne(1);
        assertEquals(600, payment.getSalary());
        assertEquals("00-12-12-12-CD", payment.getAccount());
    }

    @Test
    public void testCreate() throws Exception {
        final PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSalary(1200);
        paymentRequest.setAccount("11-22-33-44-55-66-FG");

        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.create(1, paymentRequest)).thenAnswer((Answer<PaymentResponse>) invocation -> {
            PaymentResponse payment = new PaymentResponse();
            payment.setSalary(paymentRequest.getSalary());
            payment.setAccount(paymentRequest.getAccount());
            payments.add(payment);
            return payment;
        });

        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        HttpServletResponse response = new MockHttpServletResponse();
        paymentController.create(1, paymentRequest, response);
        assertEquals("/payment/1", response.getHeader("Location"));
        assertEquals(2, payments.size());
        assertEquals(1200, payments.get(1).getSalary());
        assertEquals("11-22-33-44-55-66-FG", payments.get(1).getAccount());
    }

    @Test
    public void testUpdate() throws Exception {
        final PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSalary(890);
        paymentRequest.setAccount("00-00-99-00-FD");

        PaymentService paymentService = mock(PaymentService.class);
        when(paymentService.update(0, paymentRequest)).thenAnswer((Answer<PaymentResponse>) invocation -> {
            PaymentResponse cur = payments.get(0);
            cur.setSalary(paymentRequest.getSalary());
            cur.setAccount(paymentRequest.getAccount());
            return cur;
        });

        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        PaymentResponse ret = paymentController.update(0, paymentRequest);
        assertEquals(890, ret.getSalary());
        assertEquals("00-00-99-00-FD", ret.getAccount());
    }

    @Test
    public void testDelete() throws  Exception {
        PaymentService paymentService = mock(PaymentService.class);
        PaymentController paymentController = new PaymentController();
        ReflectionTestUtils.setField(paymentController, "paymentService", paymentService);

        paymentController.delete(0);
        verify(paymentService).delete(0);
    }
}
