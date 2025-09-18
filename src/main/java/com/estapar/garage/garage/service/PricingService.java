package com.estapar.garage.garage.service;

import com.estapar.garage.garage.domain.Garage;
import com.estapar.garage.garage.repository.GarageRepository;
import com.estapar.garage.garage.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
@RequiredArgsConstructor
public class PricingService {

    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;

    @Value("${pricing.free-minutes:30}")
    private int freeMinutes;

    @Value("${pricing.discount-low:0.90}")
    private BigDecimal discountLow;

    @Value("${pricing.surcharge-med:1.10}")
    private BigDecimal surchargeMed;

    @Value("${pricing.surcharge-high:1.25}")
    private BigDecimal surchargeHigh;

    public BigDecimal calculate(String sector, long minutes) {
        if (sector == null || sector.isBlank()) throw new IllegalArgumentException("sector é obrigatório");
        if (minutes < 0) throw new IllegalArgumentException("minutes não pode ser negativo");

        Garage garage = garageRepository.findBySector(sector)
                .orElseThrow(() -> new IllegalStateException("Setor não encontrado: " + sector));

        BigDecimal basePrice = garage.getBasePrice() != null ? garage.getBasePrice() : BigDecimal.ZERO;

        if (minutes <= freeMinutes) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long billableMinutes = minutes - freeMinutes;
        long hours = (long) Math.ceil(billableMinutes / 60.0);
        BigDecimal finalPrice = basePrice.multiply(BigDecimal.valueOf(hours));

        Integer capacity = garage.getMaxCapacity();
        if (capacity != null && capacity > 0) {
            long occupied = spotRepository.countBySectorAndOccupiedTrue(sector);
            double occupancyRate = (double) occupied / capacity;

            if (occupancyRate < 0.25) {
                finalPrice = finalPrice.multiply(discountLow);
            } else if (occupancyRate < 0.50) {
                // sem ajuste
            } else if (occupancyRate < 0.75) {
                finalPrice = finalPrice.multiply(surchargeMed);
            } else {
                finalPrice = finalPrice.multiply(surchargeHigh);
            }
        }

        if (finalPrice.signum() < 0) finalPrice = BigDecimal.ZERO;
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}