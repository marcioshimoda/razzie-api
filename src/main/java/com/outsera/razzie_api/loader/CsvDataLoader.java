package com.outsera.razzie_api.loader;

import com.outsera.razzie_api.entity.Movie;
import com.outsera.razzie_api.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Value("${razzie.csv.file:Movielist.csv}")
    private String csvFile;

    @Override
    public void run(String... args) throws Exception {

        InputStream inputStream;

        log.info("Carregando CSV {}", csvFile);

        if (csvFile.startsWith("classpath:") || !csvFile.contains("/")) {
            inputStream = new ClassPathResource(csvFile.replace("classpath:", ""))
                    .getInputStream();
        } else {
            inputStream = new FileInputStream(csvFile);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine(); // Pula o cabeçalho

            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(";");

                Integer year = Integer.parseInt(fields[0]);
                String title = fields[1];
                String studios = fields[2];
                String producers = fields[3];
                Boolean winner = fields.length > 4 && "yes".equalsIgnoreCase(fields[4]);

                Movie movie = new Movie(year, title, studios, producers, winner);

                movieRepository.save(movie);
            }
        }

        log.info("CSV carregado com sucesso!");
    }
}
