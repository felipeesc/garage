package com.estapar.garage.garage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "license_plate", length = 20, nullable = false)
    private String licensePlate;

    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "price_paid", precision = 10, scale = 2)
    private BigDecimal pricePaid;

    @Column(name = "sector", length = 10, nullable = false)
    private String sector;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;



}