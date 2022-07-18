package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/api/v1/movie")
    private List<Movie> findAllMovies() {
        return movieService.findAllMovies();
    }

    @GetMapping("/v1/movie/random")
    private List<Movie> findRandomMovies() {
        return movieService.findRandomMovies();
    }

    @GetMapping("/v1/movie/genre/{genreId}")
    private List<Movie> findMoviesByGenreId(@PathVariable int genreId) {
        return movieService.findMoviesByGenreId(genreId);
    }
}