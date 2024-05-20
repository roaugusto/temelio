package com.temelio.nonprofit.email.controller;

import com.temelio.nonprofit.email.dto.OrganizationChangeDTO;
import com.temelio.nonprofit.email.dto.OrganizationDTO;
import com.temelio.nonprofit.email.dto.Response;
import com.temelio.nonprofit.email.service.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizations")
@Tag(name = "Organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrganizationDTO>> getOrganizationById(@PathVariable UUID id) {
        OrganizationDTO organizationDTO = organizationService.findById(id);

        Response<OrganizationDTO> response = new Response<>(HttpStatus.OK.value(), "OK", organizationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<List<OrganizationDTO>>> getAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.findAll();
        Response<List<OrganizationDTO>> response = new Response<>(HttpStatus.OK.value(), "OK", organizations);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<OrganizationDTO>> saveOrganization(@RequestBody OrganizationChangeDTO organizationDTO) {
        OrganizationDTO newOrganization = organizationService.save(organizationDTO);

        Response<OrganizationDTO> response = new Response<>(HttpStatus.CREATED.value(), "Organization created!", newOrganization);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<OrganizationDTO>> updateOrganization(@PathVariable UUID id, @RequestBody OrganizationChangeDTO organizationDTO) {
        OrganizationDTO updatedOrganization = organizationService.update(id, organizationDTO);

        Response<OrganizationDTO> response = new Response<>(HttpStatus.OK.value(), "Organization updated!", updatedOrganization);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteOrganization(@PathVariable UUID id) {
        organizationService.deleteById(id);

        Response<Void> response = new Response<>(HttpStatus.NO_CONTENT.value(), "Organization deleted!", null);
        return ResponseEntity.ok(response);
    }
}
