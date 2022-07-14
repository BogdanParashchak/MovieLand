package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/api/v1/movie")
    private List<Movie> findAllMovies() {
        return movieService.findAll();
    }
}