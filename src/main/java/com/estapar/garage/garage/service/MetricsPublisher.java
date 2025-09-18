package com.estapar.garage.garage.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsPublisher {
    private final MeterRegistry registry;

    public MetricsPublisher(MeterRegistry registry) {
        this.registry = registry;
    }

    public void countEvent(String type) {
        registry.counter("garage_events_total", "type", type).increment();
    }

    public void setOccupiedSpots(String sector, long value) {
        registry.gauge("garage_spots_occupied", java.util.List.of(io.micrometer.core.instrument.Tag.of("sector", sector)), value);
    }

}
