package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.BadRequestException;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final int RANDOM_MOVIES_COUNT = 3;
    private final MovieRepository movieRepository;

    @Override
    public List<Movie> findRandomMovies() {
        List<Movie> movies = movieRepository.findAll();
        validateMovies(movies);
        Collections.shuffle(movies);
        return movies.subList(0, RANDOM_MOVIES_COUNT);
    }

    @Override
    public List<Movie> findMoviesByGenreId(int genreId, Map<String, String> requestParameters) {

        if (requestParameters.size() > 1) {
            throw new BadRequestException();
        }

        int entitiesCount = (int) movieRepository.count();
        if (entitiesCount == 0) {
            throw new MoviesNotFoundException();
        }

        if (requestParameters.isEmpty()) {
            Pageable page = PageRequest.of(0, entitiesCount);
            List<Movie> movies = movieRepository.findMoviesByGenreId(genreId, page);
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("rating"), "desc")) {
            Pageable page = PageRequest.of(0, entitiesCount, Sort.by("rating").descending());
            List<Movie> movies = movieRepository.findMoviesByGenreId(genreId, page);
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("price"), "desc")) {
            Pageable page = PageRequest.of(0, entitiesCount, Sort.by("price").descending());
            List<Movie> movies = movieRepository.findMoviesByGenreId(genreId, page);
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("price"), "asc")) {
            Pageable page = PageRequest.of(0, entitiesCount, Sort.by("price").ascending());
            List<Movie> movies = movieRepository.findMoviesByGenreId(genreId, page);
            return validateMovies(movies);
        }

        throw new BadRequestException();
    }

    @Override
    public List<Movie> findAllMovies(Map<String, String> requestParameters) {

        if (requestParameters.size() > 1) {
            throw new BadRequestException();
        }

        if (requestParameters.isEmpty()) {
            List<Movie> movies = movieRepository.findAll();
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("rating"), "desc")) {
            List<Movie> movies = movieRepository.findAll(Sort.by("rating").descending());
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("price"), "desc")) {
            List<Movie> movies = movieRepository.findAll(Sort.by("price").descending());
            return validateMovies(movies);
        }

        if (Objects.equals(requestParameters.get("price"), "asc")) {
            List<Movie> movies = movieRepository.findAll(Sort.by("price").ascending());
            return validateMovies(movies);
        }
        throw new BadRequestException();
    }

    private List<Movie> validateMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            throw new MoviesNotFoundException();
        }
        return movies;
    }
}