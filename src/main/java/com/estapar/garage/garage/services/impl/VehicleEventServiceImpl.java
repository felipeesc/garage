package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.dto.VehicleDTO;
import com.estapar.garage.garage.services.VehicleEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleEventServiceImpl implements VehicleEventService {

    private final ParkingServiceImpl parkingServiceImpl;

    public ResponseEntity<String> processEvent(VehicleDTO event) {
         if (event.getEventType() == null) {
            return ResponseEntity.badRequest().body("eventType é obrigatório");
        }
        log.info("Processando evento: {} para placa: {}", event.getEventType(), event.getLicensePlate());

        switch (event.getEventType()) {
            case ENTRY -> parkingServiceImpl.handleEntry(event);
            case PARKED -> parkingServiceImpl.handleParked(event);
            case EXIT -> parkingServiceImpl.handleExit(event);
            default -> throw new IllegalArgumentException("Evento desconhecido: " + event.getEventType());
        }

        log.info("Evento {} processado com sucesso para placa: {}", event.getEventType(), event.getLicensePlate());


        return ResponseEntity.ok("Evento " + event.getEventType() + " processado");
    }
}
