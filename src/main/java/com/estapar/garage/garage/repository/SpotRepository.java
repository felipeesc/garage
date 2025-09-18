package com.estapar.garage.garage.repository;

import com.estapar.garage.garage.domain.Spot;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    long countBySectorAndOccupiedTrue(String sector);

    Optional<Spot> findByLatAndLng(Double lat, Double lng);

    Optional<Spot> findByOccupiedAndSector(Boolean occupied, String sector);

    Optional<Spot> findByIdAndSector(Long id, String sector);

    Optional<Spot> findByVehiclePlate(@NotBlank(message = "licensePlate é obrigatório") String licensePlate);
}
