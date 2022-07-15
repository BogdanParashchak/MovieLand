package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findAll();

    List<Movie> findRandomMovies();
}