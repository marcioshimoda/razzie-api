package com.outsera.razzie_api.entity;

import jakarta.persistence.*;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_year")
    private Integer year;

    private String title;

    private String studios;

    @Column(length = 1000)
    private String producers;

    private Boolean winner;

    public Movie() {
    }

    public Movie(Integer year, String title, String studios, String producers, Boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getStudios() {
        return studios;
    }

    public String getProducers() {
        return producers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
}
