package com.estapar.garage.garage.services;

import java.math.BigDecimal;

public interface PricingService {
    BigDecimal calculate(String sector, long minutes);
}
