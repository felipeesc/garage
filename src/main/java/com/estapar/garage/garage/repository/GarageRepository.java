package com.estapar.garage.garage.repository;

import com.estapar.garage.garage.domain.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findBySector(String sector);
}
