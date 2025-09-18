package com.estapar.garage.garage.dto;

import com.estapar.garage.garage.enums.EventTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDTO {
    @NotBlank(message = "licensePlate é obrigatório")
    private String licensePlate;

    @NotNull(message = "eventType é obrigatório")
    private EventTypeEnum eventType;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double lat;
    private Double lng;
    private String sector;


}