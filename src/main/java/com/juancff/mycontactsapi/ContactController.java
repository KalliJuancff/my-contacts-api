package com.juancff.mycontactsapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class ContactController {
    @PostMapping("/contacts")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest request) {
        URI location = URI.create("/contacts/1");
        ContactResponse response = new ContactResponse(1);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<?> getContact(@PathVariable int contactId) {
        if (contactId != 1) {
            Map<String, String> map = Map.of("error", "Contact not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        ContactDetailsResponse response = new ContactDetailsResponse(contactId, "Jane Doe", "987543210");
        return ResponseEntity.ok().body(response);
    }
}
