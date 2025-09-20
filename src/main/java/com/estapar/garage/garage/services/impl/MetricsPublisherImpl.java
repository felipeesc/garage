package com.estapar.garage.garage.services.impl;

import com.estapar.garage.garage.services.MetricsPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetricsPublisherImpl implements MetricsPublisher {
    private final MeterRegistry registry;

    @Override
    public void countEvent(String type) {
        registry.counter("garage_events_total", "type", type).increment();
    }

    @Override
    public void setOccupiedSpots(String sector, long value) {
        registry.gauge("garage_spots_occupied",
                java.util.List.of(io.micrometer.core.instrument.Tag.of("sector", sector)),
                value);
    }
}

