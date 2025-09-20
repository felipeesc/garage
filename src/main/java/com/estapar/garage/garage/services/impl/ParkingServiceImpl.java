package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.domain.Garage;
import com.estapar.garage.garage.domain.Spot;
import com.estapar.garage.garage.domain.Vehicle;
import com.estapar.garage.garage.dto.VehicleDTO;
import com.estapar.garage.garage.repository.GarageRepository;
import com.estapar.garage.garage.repository.SpotRepository;
import com.estapar.garage.garage.repository.VehicleRepository;
import com.estapar.garage.garage.services.ParkingService;
import com.estapar.garage.garage.services.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingServiceImpl implements ParkingService {

    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final PricingService pricingService;

    @Override
    @Transactional
    public void handleEntry(VehicleDTO event) {
        log.info("Registrando entrada do veículo {}", event.getLicensePlate());
        Garage garage = garageRepository.findBySector(event.getSector())
                .orElseThrow(() -> new IllegalStateException("Setor não encontrado: " + event.getSector()));

        long occupied = spotRepository.countBySectorAndOccupiedTrue(event.getSector());
        if (occupied >= garage.getMaxCapacity()) {
            throw new IllegalStateException("Setor " + event.getSector() + " está lotado!");
        }


        Vehicle vehicle = vehicleRepository.findById(event.getLicensePlate())
                .orElseGet(() -> {
                    Vehicle v = new Vehicle();
                    v.setLicensePlate(event.getLicensePlate());
                    return v;
                });

        if (vehicle.getEntryTime() == null || (vehicle.getExitTime() != null && !vehicle.getExitTime().isAfter(vehicle.getEntryTime()))) {
            vehicle.setEntryTime(event.getEntryTime() != null ? event.getEntryTime() : LocalDateTime.now());
            vehicle.setExitTime(null);
        }

        vehicleRepository.save(vehicle);
        log.info("Veículo {} entrou no setor {}", event.getLicensePlate(), event.getSector());
    }

    @Override
    @Transactional
    public void handleParked(VehicleDTO event) {
        log.info("Registrando posição de estacionamento do veículo {}", event.getLicensePlate());

        Spot spot = spotRepository.findByLatAndLng(event.getLat(), event.getLng())
                .orElseThrow(() -> new IllegalStateException("Vaga não encontrada para lat/lng"));

        if (spot.isOccupied()) {
            throw new IllegalStateException("Vaga já ocupada!");
        }

        Optional<Vehicle> optVehicle = vehicleRepository.findById(event.getLicensePlate());
        if (optVehicle.isEmpty()) {
            throw new IllegalStateException("Veículo não encontrado para associar vaga!");
        }

        spot.setOccupied(Boolean.TRUE);
        spot.setVehiclePlate(event.getLicensePlate());
        spotRepository.save(spot);

        log.info("Veículo {} estacionado na vaga {}", event.getLicensePlate(), spot.getId());
    }

    @Override
    @Transactional
    public void handleExit(VehicleDTO event) {
        log.info("Registrando saída do veículo {}", event.getLicensePlate());

        Vehicle vehicle = vehicleRepository.findById(event.getLicensePlate())
                .orElseThrow(() -> new IllegalStateException("Veículo não encontrado: " + event.getLicensePlate()));

        LocalDateTime exitTime = event.getExitTime() != null ? event.getExitTime() : LocalDateTime.now();
        vehicle.setExitTime(exitTime);

        long minutes = Duration.between(vehicle.getEntryTime(), exitTime).toMinutes();

        Optional<Spot> currentSpotOpt = spotRepository.findByVehiclePlate(event.getLicensePlate());
        String sector = currentSpotOpt
                .map(Spot::getSector)
                .orElseThrow(() -> new IllegalStateException("Vaga do veículo não encontrada para cálculo de preço"));

        BigDecimal price = pricingService.calculate(sector, minutes);
        vehicle.setPricePaid(price);

        currentSpotOpt.ifPresent(spot -> {
            spot.setOccupied(Boolean.FALSE);
            spot.setVehiclePlate(null);
            spotRepository.save(spot);
        });

        vehicleRepository.save(vehicle);

        log.info("Veículo {} saiu, tempo total: {} min, valor cobrado: R${}", vehicle.getLicensePlate(), minutes, price);
    }
}