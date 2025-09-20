package com.estapar.garage.garage.services;

public interface MetricsPublisher {
    void countEvent(String type);
    void setOccupiedSpots(String sector, long value);
}

