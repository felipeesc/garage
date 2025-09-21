package com.estapar.garage.garage.controllers;

import com.estapar.garage.garage.services.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getRevenue(
            @RequestParam String date,
            @RequestParam(required = false) String sector) {

        return ResponseEntity.ok(revenueService.getRevenue(date, sector));
    }
}
