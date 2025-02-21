package com.juancff.mycontactsapi;

import java.util.Collection;

public interface ContactRepository {
    Contact save(String name, String phoneNumber);

    Contact findById(int contactId);

    Collection<Contact> findAll();
}
