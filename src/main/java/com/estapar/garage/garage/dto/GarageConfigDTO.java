package com.estapar.garage.garage.dto;

import lombok.Data;
import java.util.List;

@Data
public class GarageConfigDTO {
    private List<GarageDTO> garage;
    private List<SpotDTO> spots;

    @Data
    public static class GarageDTO {
        private String sector;
        private double basePrice;
        private int max_capacity;
    }

    @Data
    public static class SpotDTO {
        private Long id;
        private String sector;
        private double lat;
        private double lng;
    }
}
