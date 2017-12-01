package org.osergey.payment.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "payment", schema = "payment")
public class Payment {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "salary")
    private int salary;

    @Column(name = "payment")
    private String account;
}