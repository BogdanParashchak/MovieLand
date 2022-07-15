package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final int RANDOM_MOVIES_COUNT = 3;
    private final MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new MoviesNotFoundException();
        }
        return movies;
    }

    @Override
    public List<Movie> findRandomMovies() {
        List<Movie> movies = findAll();
        Collections.shuffle(movies);
        return movies.subList(0, RANDOM_MOVIES_COUNT);
    }
}