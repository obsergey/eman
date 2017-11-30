package org.osergey.contact.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.contact.domain.ContactEntity;

@Data
@NoArgsConstructor
@ToString
public class Contact {
    private String name;
    private String phone;

    public Contact(ContactEntity contact) {
        name = contact.getName();
        phone = contact.getPhone();
    }
}
