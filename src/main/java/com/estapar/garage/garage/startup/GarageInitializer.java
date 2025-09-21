package com.estapar.garage.garage.startup;

import com.estapar.garage.garage.domain.Garage;
import com.estapar.garage.garage.domain.Spot;
import com.estapar.garage.garage.dto.GarageConfigDTO;
import com.estapar.garage.garage.repository.GarageRepository;
import com.estapar.garage.garage.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class GarageInitializer implements ApplicationRunner {

    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;
    private final WebClient webClient;

    @Value("${app.simulator.base-url:http://localhost:8080}")
    private String simulatorBaseUrl;

    @Override
    public void run(ApplicationArguments args) {
        webClient.get()
                .uri(simulatorBaseUrl + "/garage")
                .retrieve()
                .bodyToMono(GarageConfigDTO.class)
                .doOnError(e -> log.error("Erro ao chamar simulador", e))
                .subscribe(dto -> {
                    try {
                        processGarageConfig(dto);
                    } catch (Exception e) {
                        log.error("Erro ao processar configuração da garagem", e);
                    }
                });
    }

    @Transactional
    public void processGarageConfig(GarageConfigDTO dto) {
        dto.getGarage().forEach(g -> {
            Garage garage = garageRepository.findBySector(g.getSector())
                    .orElse(new Garage());
            garage.setSector(g.getSector());
            garage.setBasePrice(BigDecimal.valueOf(g.getBasePrice()));
            garage.setMaxCapacity(g.getMax_capacity());
            garageRepository.save(garage);
        });

        dto.getSpots().forEach(s -> {
            Spot spot = spotRepository.findById(s.getId())
                    .orElse(new Spot());
            spot.setId(s.getId());
            spot.setSector(s.getSector());
            spot.setLat(s.getLat());
            spot.setLng(s.getLng());
            spot.setOccupied(s.getVehiclePlate() != null);
            spot.setVehiclePlate(s.getVehiclePlate());

            try {
                spotRepository.save(spot);
            } catch (OptimisticLockingFailureException e) {
                log.warn("Conflito de versão no spot {}: {}", s.getId(), e.getMessage());
            }
        });

        log.info("GarageInitializer carregou {} setores e {} vagas",
                dto.getGarage().size(), dto.getSpots().size());
    }
}
