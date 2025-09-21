package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.domains.Garage;
import com.estapar.garage.garage.repositorys.GarageRepository;
import com.estapar.garage.garage.repositorys.SpotRepository;
import com.estapar.garage.garage.services.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingServiceImpl implements PricingService {

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

    @Override
    public BigDecimal calculate(String sector, long minutes) {
        log.info("Calculando preço. sector={}, minutes={}", sector, minutes);

        if (sector == null || sector.isBlank()) {
            log.warn("Falha no cálculo: sector vazio/nulo");
            throw new IllegalArgumentException("Setor é obrigatório");
        }
        if (minutes < 0) {
            log.warn("Falha no cálculo: minutes negativo ({})", minutes);
            throw new IllegalArgumentException("minutos não pode ser negativo");
        }

        Garage garage = garageRepository.findBySector(sector)
                .orElseThrow(() -> {
                    log.warn("Setor não encontrado: {}", sector);
                    return new IllegalStateException("Setor não encontrado: " + sector);
                });

        BigDecimal basePrice = garage.getBasePrice() != null ? garage.getBasePrice() : BigDecimal.ZERO;
        if (garage.getBasePrice() == null) {
            log.warn("BasePrice nulo para sector={}, usando ZERO", sector);
        }

        if (minutes <= freeMinutes) {
            BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            log.info("Aplicando franquia grátis ({} min). Preço final={}", freeMinutes, zero);
            return zero;
        }

        long billableMinutes = minutes - freeMinutes;
        long hours = (long) Math.ceil(billableMinutes / 60.0);
        BigDecimal finalPrice = basePrice.multiply(BigDecimal.valueOf(hours));

        Integer capacity = garage.getMaxCapacity();
        if (capacity == null || capacity <= 0) {
            log.warn("Capacidade inválida para setor={} (capacidade={}), pulando ajuste por ocupação", sector, capacity);
        } else {
            long occupied = spotRepository.countBySectorAndOccupiedTrue(sector);
            double occupancyRate = (double) occupied / capacity;
            log.debug("Ocupação setor={}, ocupado={}, capacidade={}, rate={}", sector, occupied, capacity, occupancyRate);

            if (occupancyRate < 0.25) {
                finalPrice = finalPrice.multiply(discountLow);
                log.debug("Aplicando desconto LOW {} para rate<0.25", discountLow);
            } else if (occupancyRate < 0.50) {
                log.debug("Sem ajuste para 0.25<=rate<0.50");
            } else if (occupancyRate < 0.75) {
                finalPrice = finalPrice.multiply(surchargeMed);
                log.debug("Aplicando sobretaxa MED {}", surchargeMed);
            } else {
                finalPrice = finalPrice.multiply(surchargeHigh);
                log.debug("Aplicando sobretaxa HIGH {}", surchargeHigh);
            }
        }

        if (finalPrice.signum() < 0) {
            log.warn("Preço negativo calculado ({}). Ajustando para ZERO", finalPrice);
            finalPrice = BigDecimal.ZERO;
        }

        BigDecimal result = finalPrice.setScale(2, RoundingMode.HALF_UP);
        log.info("Preço calculado. setor={}, minuto={}, final={}", sector, minutes, result);
        return result;
    }
}