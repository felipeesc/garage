package com.estapar.garage.garage.services;

import com.estapar.garage.garage.dtos.VehicleDTO;

public interface ParkingService {

    void handleEntry(VehicleDTO event);
    void handleParked(VehicleDTO event);
    void handleExit(VehicleDTO event);
}
