package com.estapar.garage.garage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "spot")
public class Spot {

    @Id
    private Long id;

    @Column(nullable = false, length = 10)
    private String sector;

    @Version
    private Long version;

    private Double lat;
    private Double lng;

    private Boolean occupied = false;

    @Column(name = "vehicle_plate", length = 20)
    private String vehiclePlate;

    public boolean isOccupied() {
        return Boolean.TRUE.equals(occupied);
    }
}