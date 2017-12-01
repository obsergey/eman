package org.osergey.payment.service;

import org.osergey.payment.domain.PaymentEntity;
import org.osergey.payment.model.PaymentResponse;
import org.osergey.payment.model.PaymentRequest;
import org.osergey.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service("localPaymentService")
@ConditionalOnProperty("payment.micro.service")
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse findOne(int id) {
        return new PaymentResponse(paymentRepository.findOne(id));
    }

    @Override
    public PaymentResponse create(int id, PaymentRequest payment) {
        if(paymentRepository.exists(id)) {
            throw new EntityExistsException("PaymentResponse {" + id + "} already exists");
        }
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setSalary(payment.getSalary());
        entity.setAccount(payment.getAccount());
        return new PaymentResponse(paymentRepository.save(entity));
    }

    @Override
    public PaymentResponse update(int id, PaymentRequest payment) {
        PaymentEntity entity = paymentRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("PaymentResponse {" + id + "} not found");
        }
        entity.setSalary(payment.getSalary() > 0 ? payment.getSalary() : entity.getSalary());
        entity.setAccount(payment.getAccount() != null ? payment.getAccount() : entity.getAccount());
        return new PaymentResponse(paymentRepository.save(entity));
    }

    @Override
    public void delete(int id) {
        paymentRepository.delete(id);
    }
}
