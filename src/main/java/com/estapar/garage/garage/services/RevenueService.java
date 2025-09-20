package com.estapar.garage.garage.services;

import java.util.Map;

public interface RevenueService {

    Map<String, Object> getRevenue(String date, String sector);
}
