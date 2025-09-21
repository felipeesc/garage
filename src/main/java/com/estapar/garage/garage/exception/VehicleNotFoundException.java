package com.estapar.garage.garage.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String plate) {
        super("Veículo não encontrado: " + plate);
    }
}

