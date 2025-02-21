package com.juancff.mycontactsapi;

import java.util.Collection;
import java.util.Optional;

public interface ContactRepository {
    Contact save(String name, String phoneNumber);
    Optional<Contact> findById(int contactId);
    Collection<Contact> findAll();
}
