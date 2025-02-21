package com.juancff.mycontactsapi;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public Contact createContact(String name, String phoneNumber) {
        return repository.save(name, phoneNumber);
    }

    public Optional<Contact> getContactById(int contactId) {
        return repository.findById(contactId);
    }

    public Collection<Contact> getAllContacts() {
        return repository.findAll();
    }
}
