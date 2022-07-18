package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Genre;
import com.parashchak.movieland.exception.GenresNotFoundException;
import com.parashchak.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new GenresNotFoundException();
        }
        return genres;
    }
}