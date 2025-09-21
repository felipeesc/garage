package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.repository.GarageRepository;
import com.estapar.garage.garage.repository.SpotRepository;
import com.estapar.garage.garage.services.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;

    @Override
    public void initializeGarage() {
    }

    @Override
    public long getOccupiedSpots(String sector) {
        return spotRepository.countBySectorAndOccupiedTrue(sector);
    }

    @Override
    public long getFreeSpots(String sector) {
        return garageRepository.findBySector(sector)
                .map(g -> g.getMaxCapacity() - spotRepository.countBySectorAndOccupiedTrue(sector))
                .orElse(0L);
    }
}
