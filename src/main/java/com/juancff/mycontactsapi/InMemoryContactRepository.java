package com.juancff.mycontactsapi;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryContactRepository implements ContactRepository {
    private final Map<Integer, Contact> items = new HashMap<>();
    private int nextId = 1;

    @Override
    public Contact save(String name, String phoneNumber) {
        Contact contact = new Contact(nextId, name, phoneNumber);
        items.put(nextId, contact);
        nextId++;
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
