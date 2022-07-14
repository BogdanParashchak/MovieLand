package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MovieServiceImplTest {

    @MockBean
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Test
    void givenListOfMovies_whenFindAll_thenListOfMoviesReturned() {

        //prepare
        Movie firstMockMovie = Movie.builder()
                .id(1)
                .translatedName("первый_фильм")
                .originalName("first_movie")
                .releaseDate(LocalDate.of(2022, Month.JULY, 14))
                .description("first_movie_description")
                .price(150.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png")
                .rating(9.5)
                .build();

        Movie secondMockMovie = Movie.builder()
                .id(2)
                .translatedName("второй_фильм")
                .originalName("second_movie")
                .releaseDate(LocalDate.of(1990, Month.JANUARY, 1))
                .description("second_movie_description")
                .price(250.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg")
                .rating(7.6)
                .build();

        List<Movie> expectedMovies = List.of(firstMockMovie, secondMockMovie);
        Mockito.when(movieRepository.findAll()).thenReturn(expectedMovies);

        //when
        List<Movie> actualProducts = movieService.findAll();

        //then
        assertEquals(expectedMovies, actualProducts);
        verify(movieRepository).findAll();
    }

    @Test
    void givenEmptyListOfMovies_whenFindAll_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Mockito.when(movieRepository.findAll()).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findAll());
        verify(movieRepository).findAll();
    }
}