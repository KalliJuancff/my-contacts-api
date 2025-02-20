package com.juancff.mycontactsapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ContactController {
    @PostMapping("/contacts")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest request) {
        URI location = URI.create("/contacts/1");
        ContactResponse response = new ContactResponse(1);
        return ResponseEntity.created(location).body(response);
    }
}
