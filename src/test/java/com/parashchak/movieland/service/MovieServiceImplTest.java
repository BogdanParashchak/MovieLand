package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(PER_CLASS)
class MovieServiceImplTest {

    @MockBean
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    private final List<Movie> expectedMovies = new ArrayList<>();

    @BeforeAll
    void setUp() {
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

        Movie thirdMockMovie = Movie.builder()
                .id(3)
                .translatedName("третий_фильм")
                .originalName("third_movie")
                .releaseDate(LocalDate.of(1970, Month.JULY, 25))
                .description("third_movie_description")
                .price(350.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg")
                .rating(6.9)
                .build();

        expectedMovies.add(firstMockMovie);
        expectedMovies.add(secondMockMovie);
        expectedMovies.add(thirdMockMovie);
    }

    @Test
    void givenListOfMovies_whenFindAll_thenListOfMoviesReturned() {

        //prepare
        Mockito.when(movieRepository.findAll()).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findAll();

        //then
        assertEquals(expectedMovies, actualMovies);
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

    @Test
    void givenListOfMovies_whenFindAll_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Movie fourthMockMovie = Movie.builder()
                .id(4)
                .translatedName("четвертый_фильм")
                .originalName("fourth_movie")
                .releaseDate(LocalDate.of(1900, Month.JULY, 15))
                .description("fourth_movie_description")
                .price(450.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg")
                .rating(8.1)
                .build();

        Movie fifthMockMovie = Movie.builder()
                .id(5)
                .translatedName("пятый_фильм")
                .originalName("firth_movie")
                .releaseDate(LocalDate.of(1999, Month.APRIL, 11))
                .description("fifth_movie_description")
                .price(550.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg")
                .rating(5.2)
                .build();

        List<Movie> movies = new ArrayList<>(expectedMovies);
        movies.add(fourthMockMovie);
        movies.add(fifthMockMovie);

        Mockito.when(movieRepository.findAll()).thenReturn(movies);

        //when
        List<Movie> randomMovies = movieService.findRandomMovies();

        //then
        assertTrue(movies.containsAll(randomMovies));
        assertEquals(3, randomMovies.size());
        verify(movieRepository).findAll();
    }
}