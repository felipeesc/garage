package com.estapar.garage.garage.repositorys;

import com.estapar.garage.garage.domains.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    @Query("SELECT COALESCE(SUM(v.pricePaid), 0) " +
            "FROM Vehicle v " +
            "WHERE DATE(v.exitTime) = :date " +
            "AND (:sector IS NULL OR v.spot.sector = :sector)")
    BigDecimal getRevenueByDateAndSector(@Param("date") LocalDate date,
                                         @Param("sector") String sector);

}

