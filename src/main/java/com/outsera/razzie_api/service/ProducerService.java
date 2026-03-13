package com.outsera.razzie_api.service;

import com.outsera.razzie_api.dto.IntervalResponseDTO;
import com.outsera.razzie_api.dto.ProducerIntervalDTO;
import com.outsera.razzie_api.entity.Movie;
import com.outsera.razzie_api.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final MovieRepository movieRepository;

    public IntervalResponseDTO getAwardIntervals() {

        List<Movie> winners = movieRepository.findByWinnerTrue();
        Map<String, List<Integer>> producerWins = buildProducerWinsMap(winners);

        return calculateIntervals(producerWins);
    }

    private List<String> parseProducers(String producers) {

        return Arrays.stream(producers.split(",\\s*|\\s+and\\s+"))
                .map(String::trim)
                .filter(p -> !p.isBlank())
                .toList();
    }

    private Map<String, List<Integer>> buildProducerWinsMap(List<Movie> winners) {
        log.debug("Processando {} filmes vencedores e seus produtores", winners.size());

        return winners.stream()
                .flatMap(movie -> parseProducers(movie.getProducers())
                        .stream()
                        .map(producer -> Map.entry(producer, movie.getYear())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    private IntervalResponseDTO calculateIntervals(Map<String, List<Integer>> producerWins) {
        log.debug("Calculando os intervalos de prêmios para {} produtores", producerWins.size());

        int minInterval = Integer.MAX_VALUE;
        int maxInterval = Integer.MIN_VALUE;

        List<ProducerIntervalDTO> minList = new ArrayList<>();
        List<ProducerIntervalDTO> maxList = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
                String producer = entry.getKey();
                List<Integer> years = entry.getValue();
                Collections.sort(years);

                for (int i = 1; i < years.size(); i++) {
                        int interval = years.get(i) - years.get(i - 1);
                        ProducerIntervalDTO dto = new ProducerIntervalDTO(producer, interval, years.get(i - 1), years.get(i));

                        if (interval < minInterval) {
                                minInterval = interval;
                                minList.clear();
                                minList.add(dto);
                        } else if (interval == minInterval) {
                                minList.add(dto);
                        }

                        if (interval > maxInterval) {
                                maxInterval = interval;
                                maxList.clear();
                                maxList.add(dto);
                        } else if (interval == maxInterval) {
                                maxList.add(dto);
                        }
                }
        }

        return new IntervalResponseDTO(minList, maxList);
    }
}