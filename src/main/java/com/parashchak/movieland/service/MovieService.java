package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;

import java.util.List;
import java.util.Map;

public interface MovieService {

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenreId(int genreId, Map<String, String> requestParams);

    List<Movie> findAllMovies(Map<String, String> requestParams);

}