package garage.garage.entity.vehicle;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}