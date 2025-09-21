package com.estapar.garage.garage.dto;

import com.estapar.garage.garage.enums.EventTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDTO {

    @NotBlank(message = "licensePlate é obrigatório")
    @JsonProperty("license_plate")
    private String licensePlate;

    @NotNull(message = "eventType é obrigatório")
    @JsonProperty("event_type")
    private EventTypeEnum eventType;

    @JsonProperty("entry_time")
    private LocalDateTime entryTime;

    @JsonProperty("exit_time")
    private LocalDateTime exitTime;

    private Double lat;
    private Double lng;
}
