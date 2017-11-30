package org.osergey.payment.service;

import org.osergey.payment.domain.PaymentEntity;
import org.osergey.payment.model.Payment;
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
    public Payment findOne(int id) {
        return new Payment(paymentRepository.findOne(id));
    }

    @Override
    public Payment create(int id, Payment payment) {
        if(paymentRepository.exists(id)) {
            throw new EntityExistsException("Payment {" + id + "} already exists");
        }
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setSalary(payment.getSalary());
        entity.setAccount(payment.getAccount());
        return new Payment(paymentRepository.save(entity));
    }

    @Override
    public Payment update(int id, Payment payment) {
        PaymentEntity entity = paymentRepository.findOne(id);
        if(entity == null) {
            throw new EntityNotFoundException("Payment {" + id + "} not found");
        }
        entity.setSalary(payment.getSalary() > 0 ? payment.getSalary() : entity.getSalary());
        entity.setAccount(payment.getAccount() != null ? payment.getAccount() : entity.getAccount());
        return new Payment(paymentRepository.save(entity));
    }

    @Override
    public void delete(int id) {
        paymentRepository.delete(id);
    }
}
