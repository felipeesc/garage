package com.estapar.garage.garage.controllers;

import com.estapar.garage.garage.dtos.VehicleDTO;
import com.estapar.garage.garage.services.VehicleEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final VehicleEventService vehicleEventService;

    @PostMapping
    public ResponseEntity<String> receiveEvent(@Valid @RequestBody VehicleDTO event) {
        vehicleEventService.processEvent(event);
        return ResponseEntity.ok("Evento " + event.getEventType() + " processado");
    }


}
