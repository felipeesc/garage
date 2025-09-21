package com.estapar.garage.garage.repositorys;

import com.estapar.garage.garage.domains.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findBySector(String sector);
}
