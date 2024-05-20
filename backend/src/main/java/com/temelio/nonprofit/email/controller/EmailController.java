package com.temelio.nonprofit.email.controller;

import com.temelio.nonprofit.email.dto.EmailSentDTO;
import com.temelio.nonprofit.email.dto.Response;
import com.temelio.nonprofit.email.service.EmailSentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/emails")
@Tag(name = "Emails")
public class EmailController {

    private final EmailSentService emailSentService;


    @Autowired
    public EmailController(EmailSentService emailSentService) {
        this.emailSentService = emailSentService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Response<EmailSentDTO>> getEmailById(@PathVariable UUID id) {
        EmailSentDTO emailDTO = emailSentService.findById(id);

        Response<EmailSentDTO> response = new Response<>(HttpStatus.OK.value(), "OK", emailDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<List<EmailSentDTO>>> getAllEmails() {
        List<EmailSentDTO> emails = emailSentService.findAll();
        Response<List<EmailSentDTO>> response = new Response<>(HttpStatus.OK.value(), "OK", emails);
        return ResponseEntity.ok(response);
    }

}
