package com.juancff.mycontactsapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ContactController {
    private final ContactRepository repository;

    public ContactController(ContactRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/contacts")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest request) {
        Contact contact = createContact_(request.name(), request.phoneNumber());
        var contactId = contact.id();

        URI location = URI.create("/contacts/" + contactId);
        ContactResponse response = new ContactResponse(contactId);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<?> getContact(@PathVariable int contactId) {
        Optional<Contact> optionalContact = getContactById(contactId);
        if (optionalContact.isEmpty()) {
            Map<String, String> map = Map.of("error", "Contact not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        Contact contact = optionalContact.get();
        ContactDetailsResponse response = new ContactDetailsResponse(contactId, contact.name(), contact.phoneNumber());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/contacts")
    public List<ContactDetailsResponse> getContacts() {
        return getAllContacts().stream()
            .map(contact -> new ContactDetailsResponse(contact.id(), contact.name(), contact.phoneNumber()))
            .toList();
    }

    private Contact createContact_(String name, String phoneNumber) {
        return repository.save(name, phoneNumber);
    }

    private Optional<Contact> getContactById(int contactId) {
        return repository.findById(contactId);
    }

    private Collection<Contact> getAllContacts() {
        return repository.findAll();
    }
}
