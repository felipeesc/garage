package garage.garage.entity.garage;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "garage")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String sector;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "max_capacity")
    private Integer maxCapacity;


}