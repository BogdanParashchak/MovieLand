package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.BadRequestException;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.repository.MovieRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void givenListOfMovies_whenFindRandomMovies_thenThreeRandomMoviesReturned() {

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

        when(movieRepository.findAll()).thenReturn(movies);

        //when
        List<Movie> randomMovies = movieService.findRandomMovies();

        //then
        assertTrue(movies.containsAll(randomMovies));
        assertEquals(3, randomMovies.size());
        verify(movieRepository).findAll();
    }

    @Test
    void givenEmptyListOfMovies_whenFindRandomMovies_thenMoviesNotFoundExceptionThrown() {

        //prepare
        when(movieRepository.findAll()).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findRandomMovies());
        verify(movieRepository).findAll();
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithRequestParametersMapSizeMoreThanOne_thenBadRequestExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc", "price", "asc");

        //then
        assertThrows(BadRequestException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdAndEntitiesCountIsZero_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc");
        when(movieRepository.count()).thenReturn(0L);

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithNoRequestParameters_thenListOfMoviesReturned() {

        //prepare
        Map<String, String> requestMap = new HashMap<>();
        when(movieRepository.count()).thenReturn(3L);
        Pageable page = PageRequest.of(0, 3);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(expectedMovies);

        //when
        List<Movie> moviesByGenreId = movieService.findMoviesByGenreId(1, requestMap);

        //then
        assertEquals(expectedMovies, moviesByGenreId);
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenEmptyListOfMovies_whenFindMoviesByGenreIdWithNoRequestParameters_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = new HashMap<>();
        when(movieRepository.count()).thenReturn(3L);
        Pageable page = PageRequest.of(0, 3);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithRequestParameters_thenSortedByRatingDescendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc");
        Pageable page = PageRequest.of(0, 3, Sort.by("rating").descending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findMoviesByGenreId(1, requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithRatingDescendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc");
        Pageable page = PageRequest.of(0, 3, Sort.by("rating").descending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithRequestParameters_thenSortedByPriceDescendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "desc");
        Pageable page = PageRequest.of(0, 3, Sort.by("price").descending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findMoviesByGenreId(1, requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithPriceDescendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "desc");
        Pageable page = PageRequest.of(0, 3, Sort.by("price").descending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithRequestParameters_thenSortedByPriceAscendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "asc");
        Pageable page = PageRequest.of(0, 3, Sort.by("price").ascending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findMoviesByGenreId(1, requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithPriceAscendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "asc");
        Pageable page = PageRequest.of(0, 3, Sort.by("price").ascending());
        when(movieRepository.count()).thenReturn(3L);
        when(movieRepository.findMoviesByGenreId(1, page)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
        verify(movieRepository).count();
        verify(movieRepository).findMoviesByGenreId(1, page);
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreIdWithIncorrectRequestParameters_thenBadRequestExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "someSortingOrder");
        when(movieRepository.count()).thenReturn(3L);

        //then
        assertThrows(BadRequestException.class, () -> movieService.findMoviesByGenreId(1, requestMap));
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithRequestParametersMapSizeMoreThanOne_thenBadRequestExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc", "price", "asc");

        //then
        assertThrows(BadRequestException.class, () -> movieService.findAllMovies(requestMap));
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithNoRequestParameters_thenListOfMoviesReturned() {

        //prepare
        Map<String, String> requestMap = new HashMap<>();
        when(movieRepository.findAll()).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findAllMovies(requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).findAll();
    }

    @Test
    void givenEmptyListOfMovies_whenFindAllMovies_thenMoviesNotFoundExceptionThrown() {

        //prepare
        List<Movie> movies = new ArrayList<>();
        Map<String, String> requestMap = new HashMap<>();
        when(movieRepository.findAll()).thenReturn(movies);

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findAllMovies(requestMap));
        verify(movieRepository).findAll();
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithRequestParameters_thenSortedByRatingDescendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc");
        Sort sort = Sort.by("rating").descending();
        when(movieRepository.findAll(sort)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findAllMovies(requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithRatingDescendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("rating", "desc");
        Sort sort = Sort.by("rating").descending();
        when(movieRepository.findAll(sort)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findAllMovies(requestMap));
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithRequestParameters_thenSortedByPriceDescendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "desc");
        Sort sort = Sort.by("price").descending();
        when(movieRepository.findAll(sort)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findAllMovies(requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithPriceDescendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "desc");
        Sort sort = Sort.by("price").descending();
        when(movieRepository.findAll(sort)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findAllMovies(requestMap));
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithRequestParameters_thenSortedByPriceAscendingListReturned() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "asc");
        Sort sort = Sort.by("price").ascending();
        when(movieRepository.findAll(sort)).thenReturn(expectedMovies);

        //when
        List<Movie> actualMovies = movieService.findAllMovies(requestMap);

        //then
        assertEquals(expectedMovies, actualMovies);
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithPriceAscendingParameterAndNoMoviesFound_thenMoviesNotFoundExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "asc");
        Sort sort = Sort.by("price").ascending();
        when(movieRepository.findAll(sort)).thenReturn(new ArrayList<>());

        //then
        assertThrows(MoviesNotFoundException.class, () -> movieService.findAllMovies(requestMap));
        verify(movieRepository).findAll(sort);
    }

    @Test
    void givenListOfMovies_whenFindAllMoviesWithIncorrectRequestParameters_thenBadRequestExceptionThrown() {

        //prepare
        Map<String, String> requestMap = Map.of("price", "someSortingOrder");

        //then
        assertThrows(BadRequestException.class, () -> movieService.findAllMovies(requestMap));
    }
}