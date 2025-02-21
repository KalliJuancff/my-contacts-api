package com.juancff.mycontactsapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ContactController {
    private final Map<Integer, Contact> items = new HashMap<>();

    @PostMapping("/contacts")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest request) {
        Contact contact = save(request.name(), request.phoneNumber());
        var contactId = contact.id();

        URI location = URI.create("/contacts/" + contactId);
        ContactResponse response = new ContactResponse(contactId);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<?> getContact(@PathVariable int contactId) {
        var contact = findById(contactId);
        if (contact == null) {
            Map<String, String> map = Map.of("error", "Contact not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        ContactDetailsResponse response = new ContactDetailsResponse(contactId, contact.name(), contact.phoneNumber());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/contacts")
    public List<ContactDetailsResponse> getContacts() {
        return findAll().stream()
            .map(contact -> new ContactDetailsResponse(contact.id(), contact.name(), contact.phoneNumber()))
            .toList();
    }

    private Contact save(String name, String phoneNumber) {
        var contactId = items.size() + 1;
        Contact contact = new Contact(contactId, name, phoneNumber);
        items.put(contactId, contact);
        return contact;
    }

    private Contact findById(int contactId) {
        return items.get(contactId);
    }

    private Collection<Contact> findAll() {
        return items.values();
    }
}
