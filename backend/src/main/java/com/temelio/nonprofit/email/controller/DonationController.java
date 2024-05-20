package com.temelio.nonprofit.email.controller;

import com.temelio.nonprofit.email.dto.DonationChangeDTO;
import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.dto.Response;
import com.temelio.nonprofit.email.service.DonationService;
import com.temelio.nonprofit.email.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/donations")
@Tag(name = "Donations")
public class DonationController {

    private final DonationService donationService;

    private final EmailService emailService;

    @Autowired
    public DonationController(DonationService donationService, EmailService emailService) {
        this.donationService = donationService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Response<DonationDTO>> saveDonation(@RequestBody DonationChangeDTO donation) {
        DonationDTO newDonation = donationService.save(donation.getOrganizationId(), donation.getAmount());

        Response<DonationDTO> response = new Response<>(HttpStatus.CREATED.value(), "Donation created!", newDonation);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<DonationDTO>> getDonationById(@PathVariable UUID id) {
        DonationDTO donationDTO = donationService.findById(id);

        Response<DonationDTO> response = new Response<>(HttpStatus.OK.value(), "OK", donationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<List<DonationDTO>>> getAllDonations() {
        List<DonationDTO> donations = donationService.findAll();
        Response<List<DonationDTO>> response = new Response<>(HttpStatus.OK.value(), "OK", donations);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<DonationDTO>> updateDonation(@PathVariable UUID id, @RequestBody DonationChangeDTO donation) {
        DonationDTO updatedDonation = donationService.update(id, donation.getOrganizationId(), donation.getAmount(), donation.getIsDonationNotified());

        Response<DonationDTO> response = new Response<>(HttpStatus.OK.value(), "Donation updated!", updatedDonation);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteDonation(@PathVariable UUID id) {
        donationService.deleteById(id);

        Response<Void> response = new Response<>(HttpStatus.NO_CONTENT.value(), "Donation deleted!", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<Response<String>> sendEmail() {
        String result = emailService.sendDonationNotifications();

        Response<String> response = new Response<>(HttpStatus.OK.value(), result, null);
        return ResponseEntity.ok(response);
    }
}
