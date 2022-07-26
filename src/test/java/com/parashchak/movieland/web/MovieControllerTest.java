package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.BadRequestException;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.service.MovieService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(PER_CLASS)
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    private final List<Movie> expectedMovies = new ArrayList<>();

    @BeforeAll
    void setUp() {
        Movie firstMockMovie = Movie.builder().id(1).translatedName("первый_фильм").originalName("first_movie").releaseDate(LocalDate.of(2022, Month.JULY, 14)).description("first_movie_description").price(150.0).picturePath("https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png").rating(9.5).votes(100).build();

        Movie secondMockMovie = Movie.builder().id(2).translatedName("второй_фильм").originalName("second_movie").releaseDate(LocalDate.of(1990, Month.JANUARY, 1)).description("second_movie_description").price(250.0).picturePath("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg").rating(7.6).votes(200).build();

        Movie thirdMockMovie = Movie.builder().id(3).translatedName("третий_фильм").originalName("third_movie").releaseDate(LocalDate.of(1980, Month.FEBRUARY, 10)).description("third_movie_description").price(350.0).picturePath("https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg").rating(6.5).votes(300).build();

        expectedMovies.add(firstMockMovie);
        expectedMovies.add(secondMockMovie);
        expectedMovies.add(thirdMockMovie);
    }

    @Test
    void givenListOfMovies_whenFindRandomMovies_thenThreeRandomMoviesReturned() throws Exception {

        when(movieService.findRandomMovies()).thenReturn(expectedMovies);

        mockMvc.perform(get("/v1/movie/random").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isOk()).andExpect(content().json("""
                        [
                          {
                            "id": 1,
                            "translatedName": "первый_фильм",
                            "originalName": "first_movie",
                            "releaseDate": "2022-07-14",
                            "description": "first_movie_description",
                            "price": 150.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png",
                            "rating": 9.5,
                            "votes": 100
                          },
                          {
                            "id": 2,
                            "translatedName": "второй_фильм",
                            "originalName": "second_movie",
                            "releaseDate": "1990-01-01",
                            "description": "second_movie_description",
                            "price": 250.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg",
                            "rating": 7.6,
                            "votes": 200
                          },
                                                    {
                            "id": 3,
                            "translatedName": "третий_фильм",
                            "originalName": "third_movie",
                            "releaseDate": "1980-02-10",
                            "description": "third_movie_description",
                            "price": 350.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg",
                            "rating": 6.5,
                            "votes": 300
                          }
                        ]
                        """));

        verify(movieService).findRandomMovies();
    }

    @Test
    public void givenNoMovies_whenFindRandomMovies_thenNotFoundStatusReturned() throws Exception {

        when(movieService.findRandomMovies()).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/v1/movie/random").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isNotFound()).andExpect(status().reason("Movies not found"));

        verify(movieService).findRandomMovies();
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreId_thenMoviesReturned() throws Exception {

        Map<String, String> requestMap = new HashMap<>();
        when(movieService.findMoviesByGenreId(1, requestMap)).thenReturn(expectedMovies);

        mockMvc.perform(get("/v1/movie/genre/1").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isOk())

                .andExpect(content().json("""
                        [
                          {
                            "id": 1,
                            "translatedName": "первый_фильм",
                            "originalName": "first_movie",
                            "releaseDate": "2022-07-14",
                            "description": "first_movie_description",
                            "price": 150.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png",
                            "rating": 9.5,
                            "votes": 100
                          },
                          {
                            "id": 2,
                            "translatedName": "второй_фильм",
                            "originalName": "second_movie",
                            "releaseDate": "1990-01-01",
                            "description": "second_movie_description",
                            "price": 250.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg",
                            "rating": 7.6,
                            "votes": 200
                          },
                                                    {
                            "id": 3,
                            "translatedName": "третий_фильм",
                            "originalName": "third_movie",
                            "releaseDate": "1980-02-10",
                            "description": "third_movie_description",
                            "price": 350.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg",
                            "rating": 6.5,
                            "votes": 300
                          }
                        ]
                        """));

        verify(movieService).findMoviesByGenreId(1, requestMap);
    }

    @Test
    void givenNoMovies_whenFindMoviesByGenreId_thenNotFoundStatusReturned() throws Exception {

        Map<String, String> requestMap = new HashMap<>();
        when(movieService.findMoviesByGenreId(1, requestMap)).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/v1/movie/genre/1").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isNotFound()).andExpect(status().reason("Movies not found"));

        verify(movieService).findMoviesByGenreId(1, requestMap);
    }

    @Test
    void givenIncorrectRequestParameters_whenFindMoviesByGenreId_thenBadRequestStatusReturned() throws Exception {

        Map<String, String> requestMap = Map.of("rating", "desc", "price", "asc");
        when(movieService.findMoviesByGenreId(1, requestMap)).thenThrow((new BadRequestException()));

        mockMvc.perform(get("/v1/movie/genre/1?rating=desc&price=asc").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isBadRequest()).andExpect(status().reason("Check request parameters"));

        verify(movieService).findMoviesByGenreId(1, requestMap);
    }

    @Test
    public void givenMovies_whenFindAllMovies_thenMoviesReturned() throws Exception {

        Map<String, String> requestMap = new HashMap<>();
        when(movieService.findAllMovies(requestMap)).thenReturn(expectedMovies);

        mockMvc.perform(get("/v1/movie").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isOk()).andExpect(content().json("""
                        [
                          {
                            "id": 1,
                            "translatedName": "первый_фильм",
                            "originalName": "first_movie",
                            "releaseDate": "2022-07-14",
                            "description": "first_movie_description",
                            "price": 150.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png",
                            "rating": 9.5,
                            "votes": 100
                          },
                          {
                            "id": 2,
                            "translatedName": "второй_фильм",
                            "originalName": "second_movie",
                            "releaseDate": "1990-01-01",
                            "description": "second_movie_description",
                            "price": 250.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg",
                            "rating": 7.6,
                            "votes": 200
                          },
                                                    {
                            "id": 3,
                            "translatedName": "третий_фильм",
                            "originalName": "third_movie",
                            "releaseDate": "1980-02-10",
                            "description": "third_movie_description",
                            "price": 350.00,
                            "picturePath": "https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg",
                            "rating": 6.5,
                            "votes": 300
                          }
                        ]
                        """));

        verify(movieService).findAllMovies(requestMap);
    }

    @Test
    public void givenNoMovies_whenFindAllMovies_thenNotFoundStatusReturned() throws Exception {

        Map<String, String> requestMap = new HashMap<>();
        when(movieService.findAllMovies(requestMap)).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/v1/movie").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isNotFound()).andExpect(status().reason("Movies not found"));

        verify(movieService).findAllMovies(requestMap);
    }

    @Test
    public void givenIncorrectRequestParameters_whenFindAllMovies_thenBadRequestStatusReturned() throws Exception {

        Map<String, String> requestMap = Map.of("rating", "desc", "price", "asc");
        when(movieService.findAllMovies(requestMap)).thenThrow(new BadRequestException());

        mockMvc.perform(get("/v1/movie?rating=desc&price=asc").contentType(MediaType.APPLICATION_JSON).content("{}"))

                .andExpect(status().isBadRequest()).andExpect(status().reason("Check request parameters"));

        verify(movieService).findAllMovies(requestMap);
    }
}