package com.juancff.mycontactsapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryContactRepository {
    private final Map<Integer, Contact> items = new HashMap<>();

    Contact save(String name, String phoneNumber) {
        var contactId = items.size() + 1;
        Contact contact = new Contact(contactId, name, phoneNumber);
        items.put(contactId, contact);
        return contact;
    }

    Contact findById(int contactId) {
        return items.get(contactId);
    }

    Collection<Contact> findAll() {
        return items.values();
    }
}
