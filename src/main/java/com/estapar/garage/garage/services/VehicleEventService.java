package com.estapar.garage.garage.services;

import com.estapar.garage.garage.dtos.VehicleDTO;
import org.springframework.http.ResponseEntity;

public interface VehicleEventService {

    ResponseEntity<String> processEvent(VehicleDTO event);
}
