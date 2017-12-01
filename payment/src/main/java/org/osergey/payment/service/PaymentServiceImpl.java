package org.osergey.payment.service;

import org.osergey.payment.domain.Payment;
import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("localPaymentService")
@ConditionalOnProperty("payment.micro.service")
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    private Payment findChecked(int id) {
        Payment entity = paymentRepository.findOne(id);
        if(entity == null) {
            throw new PaymentNotFoundException(id);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse findOne(int id) {
        return new PaymentResponse(findChecked(id));
    }

    @Override
    @Transactional
    public PaymentResponse create(int id, PaymentRequest payment) {
        if(paymentRepository.exists(id)) {
            throw new PaymentExistsException(id);
        }
        Payment entity = new Payment();
        entity.setId(id);
        entity.setSalary(payment.getSalary());
        entity.setAccount(payment.getAccount());
        return new PaymentResponse(paymentRepository.save(entity));
    }

    @Override
    @Transactional
    public PaymentResponse update(int id, PaymentRequest payment) {
        Payment entity = findChecked(id);
        entity.setSalary(payment.getSalary() > 0 ? payment.getSalary() : entity.getSalary());
        entity.setAccount(payment.getAccount() != null ? payment.getAccount() : entity.getAccount());
        return new PaymentResponse(paymentRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(int id) {
        paymentRepository.delete(findChecked(id));
    }
}
