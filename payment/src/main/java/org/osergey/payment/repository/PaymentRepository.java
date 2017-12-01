package org.osergey.payment.repository;

import org.osergey.payment.domain.Payment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("payment.micro.service")
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
