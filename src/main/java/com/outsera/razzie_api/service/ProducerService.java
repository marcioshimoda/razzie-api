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
        List<ProducerIntervalDTO> intervals = calculateIntervals(producerWins);

        return buildResponse(intervals);
    }

    private List<String> parseProducers(String producers) {

        return Arrays.stream(producers.split(",|\\s+and\\s+"))
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

    private List<ProducerIntervalDTO> calculateIntervals(Map<String, List<Integer>> producerWins) {
        log.debug("Calculando os intervalos de prêmios para {} produtores", producerWins.size());

        List<ProducerIntervalDTO> intervals = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            List<Integer> years = entry.getValue()
                    .stream()
                    .sorted()
                    .toList();

            if (years.size() < 2) continue;

            for (int i = 1; i < years.size(); i++) {
                int previous = years.get(i - 1);
                int next = years.get(i);

                intervals.add(
                        new ProducerIntervalDTO(
                                entry.getKey(),
                                next - previous,
                                previous,
                                next
                        )
                );
            }
        }

        return intervals;
    }

    private IntervalResponseDTO buildResponse(List<ProducerIntervalDTO> intervals) {

        int minInterval = intervals.stream()
                .mapToInt(ProducerIntervalDTO::getInterval)
                .min()
                .orElse(0);
        int maxInterval = intervals.stream()
                .mapToInt(ProducerIntervalDTO::getInterval)
                .max()
                .orElse(0);

        List<ProducerIntervalDTO> minList = intervals.stream()
                .filter(i -> i.getInterval() == minInterval)
                .sorted(Comparator.comparing(ProducerIntervalDTO::getProducer))
                .toList();

        List<ProducerIntervalDTO> maxList = intervals.stream()
                .filter(i -> i.getInterval() == maxInterval)
                .sorted(Comparator.comparing(ProducerIntervalDTO::getProducer))
                .toList();

        return new IntervalResponseDTO(minList, maxList);
    }
}