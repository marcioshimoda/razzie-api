package com.outsera.razzie_api.controller;

import com.outsera.razzie_api.dto.IntervalResponseDTO;
import com.outsera.razzie_api.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/producers/awards/intervals")
    public IntervalResponseDTO getAwardIntervals() {
        return producerService.getAwardIntervals();
    }
}