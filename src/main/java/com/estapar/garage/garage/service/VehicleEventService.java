package com.estapar.garage.garage.service;

import com.estapar.garage.garage.dto.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleEventService {

    private final ParkingService parkingService;

    public ResponseEntity<String> processEvent(VehicleDTO event) {
        if (event.getEventType() == null) {
            return ResponseEntity.badRequest().body("eventType é obrigatório");
        }
        log.info("Processando evento: {} para placa: {}", event.getEventType(), event.getLicensePlate());

        switch (event.getEventType()) {
            case ENTRY -> parkingService.handleEntry(event);
            case PARKED -> parkingService.handleParked(event);
            case EXIT -> parkingService.handleExit(event);
            default -> throw new IllegalArgumentException("Evento desconhecido: " + event.getEventType());
        }

        log.info("Evento {} processado com sucesso para placa: {}", event.getEventType(), event.getLicensePlate());


        return ResponseEntity.ok("Evento " + event.getEventType() + " processado");
    }
}
