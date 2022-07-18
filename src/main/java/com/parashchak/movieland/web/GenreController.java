package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Genre;
import com.parashchak.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/v1/genre")
    private List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }
}