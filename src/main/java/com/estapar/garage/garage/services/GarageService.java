package com.estapar.garage.garage.services;

public interface GarageService {
    void initializeGarage();
    long getOccupiedSpots(String sector);
    long getFreeSpots(String sector);
}

