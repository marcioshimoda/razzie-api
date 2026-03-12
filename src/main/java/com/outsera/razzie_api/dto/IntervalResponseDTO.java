package com.outsera.razzie_api.dto;

import java.util.List;

public class IntervalResponseDTO {

    private List<ProducerIntervalDTO> min;
    private List<ProducerIntervalDTO> max;

    public IntervalResponseDTO(List<ProducerIntervalDTO> min, List<ProducerIntervalDTO> max) {
        this.min = min;
        this.max = max;
    }

    public List<ProducerIntervalDTO> getMin() { return min; }
    public List<ProducerIntervalDTO> getMax() { return max; }
}
