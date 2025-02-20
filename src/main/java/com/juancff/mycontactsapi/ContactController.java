package com.juancff.mycontactsapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ContactController {
    @PostMapping("/contacts")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest request) {
        URI location = URI.create("/contacts/1");
        ContactResponse response = new ContactResponse(1);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/contacts/{contactId}")
    public ContactDetailsResponse getContact(@PathVariable int contactId) {
        return new ContactDetailsResponse(contactId, "Jane Doe", "987543210");
    }
}
