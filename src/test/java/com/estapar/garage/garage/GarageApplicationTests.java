package com.estapar.garage.garage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Ignorando contextLoads no CI")
class GarageApplicationTests {
    @Test
    void contextLoads() {
    }
}
