package com.estapar.garage.garage.repository;

import com.estapar.garage.garage.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
