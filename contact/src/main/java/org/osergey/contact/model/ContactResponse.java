package org.osergey.contact.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.osergey.contact.domain.ContactEntity;

@Data
@NoArgsConstructor
public class ContactResponse {
    private String name;
    private String phone;

    public ContactResponse(ContactEntity contact) {
        name = contact.getName();
        phone = contact.getPhone();
    }
}
