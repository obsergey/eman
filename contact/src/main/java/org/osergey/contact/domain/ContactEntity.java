package org.osergey.contact.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contact", schema = "contact")
public class ContactEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;
}
