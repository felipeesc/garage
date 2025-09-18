package garage.garage.entity.spot;

import jakarta.persistence.*;

@Entity
@Table(name = "spot")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String sector;

    private Double lat;
    private Double lng;

    private Boolean occupied;

}