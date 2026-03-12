package com.outsera.razzie_api.repository;

import com.outsera.razzie_api.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByWinnerTrue();

}