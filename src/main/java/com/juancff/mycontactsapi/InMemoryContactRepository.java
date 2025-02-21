package com.juancff.mycontactsapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryContactRepository implements ContactRepository {
    private final Map<Integer, Contact> items = new HashMap<>();

    @Override
    public Contact save(String name, String phoneNumber) {
        var contactId = items.size() + 1;
        Contact contact = new Contact(contactId, name, phoneNumber);
        items.put(contactId, contact);
        return contact;
    }

    @Override
    public Contact findById(int contactId) {
        return items.get(contactId);
    }

    @Override
    public Collection<Contact> findAll() {
        return items.values();
    }
}
