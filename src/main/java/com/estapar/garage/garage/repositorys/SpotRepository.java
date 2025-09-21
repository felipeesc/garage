package com.estapar.garage.garage.repositorys;

import com.estapar.garage.garage.domains.Spot;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    long countBySectorAndOccupiedTrue(String sector);

    Optional<Spot> findByLatAndLng(Double lat, Double lng);

    Optional<Spot> findByVehiclePlate(@NotBlank(message = "licensePlate é obrigatório") String licensePlate);
}
