package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.repositorys.VehicleRepository;
import com.estapar.garage.garage.services.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final VehicleRepository vehicleRepository;

    public Map<String, Object> getRevenue(String date, String sector) {
        LocalDate parsedDate = LocalDate.parse(date);
        BigDecimal revenue = vehicleRepository.getRevenueByDateAndSector(parsedDate, sector);

        return Map.of(
                "amount", revenue,
                "currency", "BRL",
                "timestamp", LocalDateTime.now()
        );
    }
}
