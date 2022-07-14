package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new MoviesNotFoundException();
        }
        return movies;
    }
}