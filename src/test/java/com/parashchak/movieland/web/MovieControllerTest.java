package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Movie;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .releaseDate(LocalDate.of(1980, Month.FEBRUARY, 10))
                .description("third_movie_description")
                .price(350.0)
                .picturePath("https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg")
                .rating(6.5)
                .build();

        expectedMovies.add(firstMockMovie);
        expectedMovies.add(secondMockMovie);
        expectedMovies.add(thirdMockMovie);
    }

    @Test
    public void givenMovies_whenFindAllMovies_thenMoviesReturned() throws Exception {

        when(movieService.findAllMovies()).thenReturn(expectedMovies);

        mockMvc.perform(get("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].translatedName").value("первый_фильм"))
                .andExpect(jsonPath("$.[0].originalName").value("first_movie"))
                .andExpect(jsonPath("$.[0].releaseDate").value("2022-07-14"))
                .andExpect(jsonPath("$.[0].description").value("first_movie_description"))
                .andExpect(jsonPath("$.[0].price").value(150.0))
                .andExpect(jsonPath("$.[0].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png"))
                .andExpect(jsonPath("$.[0].rating").value(9.5))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].translatedName").value("второй_фильм"))
                .andExpect(jsonPath("$.[1].originalName").value("second_movie"))
                .andExpect(jsonPath("$.[1].releaseDate").value("1990-01-01"))
                .andExpect(jsonPath("$.[1].description").value("second_movie_description"))
                .andExpect(jsonPath("$.[1].price").value(250.0))
                .andExpect(jsonPath("$.[1].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg"))
                .andExpect(jsonPath("$.[1].rating").value(7.6))

                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].translatedName").value("третий_фильм"))
                .andExpect(jsonPath("$.[2].originalName").value("third_movie"))
                .andExpect(jsonPath("$.[2].releaseDate").value("1980-02-10"))
                .andExpect(jsonPath("$.[2].description").value("third_movie_description"))
                .andExpect(jsonPath("$.[2].price").value(350.0))
                .andExpect(jsonPath("$.[2].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg"))
                .andExpect(jsonPath("$.[2].rating").value(6.5));

        verify(movieService).findAllMovies();
    }

    @Test
    public void givenNoMovies_whenFindAllMovies_thenNotFoundStatusReturned() throws Exception {

        when(movieService.findAllMovies()).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(status().reason("Movies not found"));

        verify(movieService).findAllMovies();
    }

    @Test
    void givenListOfMovies_whenFindRandomMovies_thenThreeRandomMoviesReturned() throws Exception {

        when(movieService.findRandomMovies()).thenReturn(expectedMovies);

        mockMvc.perform(get("/v1/movie/random")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].translatedName").value("первый_фильм"))
                .andExpect(jsonPath("$.[0].originalName").value("first_movie"))
                .andExpect(jsonPath("$.[0].releaseDate").value("2022-07-14"))
                .andExpect(jsonPath("$.[0].description").value("first_movie_description"))
                .andExpect(jsonPath("$.[0].price").value(150.0))
                .andExpect(jsonPath("$.[0].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png"))
                .andExpect(jsonPath("$.[0].rating").value(9.5))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].translatedName").value("второй_фильм"))
                .andExpect(jsonPath("$.[1].originalName").value("second_movie"))
                .andExpect(jsonPath("$.[1].releaseDate").value("1990-01-01"))
                .andExpect(jsonPath("$.[1].description").value("second_movie_description"))
                .andExpect(jsonPath("$.[1].price").value(250.0))
                .andExpect(jsonPath("$.[1].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg"))
                .andExpect(jsonPath("$.[1].rating").value(7.6))

                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].translatedName").value("третий_фильм"))
                .andExpect(jsonPath("$.[2].originalName").value("third_movie"))
                .andExpect(jsonPath("$.[2].releaseDate").value("1980-02-10"))
                .andExpect(jsonPath("$.[2].description").value("third_movie_description"))
                .andExpect(jsonPath("$.[2].price").value(350.0))
                .andExpect(jsonPath("$.[2].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg"))
                .andExpect(jsonPath("$.[2].rating").value(6.5));

        verify(movieService).findRandomMovies();
    }

    @Test
    public void givenNoMovies_whenFindRandomMovies_thenNotFoundStatusReturned() throws Exception {

        when(movieService.findRandomMovies()).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/v1/movie/random")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(status().reason("Movies not found"));

        verify(movieService).findRandomMovies();
    }

    @Test
    void givenListOfMovies_whenFindMoviesByGenreId_thenMoviesReturned() throws Exception {

        when(movieService.findMoviesByGenreId(1)).thenReturn(expectedMovies);

        mockMvc.perform(get("/v1/movie/genre/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].translatedName").value("первый_фильм"))
                .andExpect(jsonPath("$.[0].originalName").value("first_movie"))
                .andExpect(jsonPath("$.[0].releaseDate").value("2022-07-14"))
                .andExpect(jsonPath("$.[0].description").value("first_movie_description"))
                .andExpect(jsonPath("$.[0].price").value(150.0))
                .andExpect(jsonPath("$.[0].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Titanic_(1997_film)_poster.png"))
                .andExpect(jsonPath("$.[0].rating").value(9.5))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].translatedName").value("второй_фильм"))
                .andExpect(jsonPath("$.[1].originalName").value("second_movie"))
                .andExpect(jsonPath("$.[1].releaseDate").value("1990-01-01"))
                .andExpect(jsonPath("$.[1].description").value("second_movie_description"))
                .andExpect(jsonPath("$.[1].price").value(250.0))
                .andExpect(jsonPath("$.[1].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Avatar_(2009_film)_poster.jpg"))
                .andExpect(jsonPath("$.[1].rating").value(7.6))

                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].translatedName").value("третий_фильм"))
                .andExpect(jsonPath("$.[2].originalName").value("third_movie"))
                .andExpect(jsonPath("$.[2].releaseDate").value("1980-02-10"))
                .andExpect(jsonPath("$.[2].description").value("third_movie_description"))
                .andExpect(jsonPath("$.[2].price").value(350.0))
                .andExpect(jsonPath("$.[2].picturePath")
                        .value("https://en.wikipedia.org/wiki/File:Interstellar_film_poster.jpg"))
                .andExpect(jsonPath("$.[2].rating").value(6.5));

        verify(movieService).findMoviesByGenreId(1);
    }

    @Test
    void givenNoMovies_whenFindMoviesByGenreId_thenNotFoundStatusReturned() throws Exception {

        when(movieService.findMoviesByGenreId(1)).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/v1/movie/genre/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(status().reason("Movies not found"));

        verify(movieService).findMoviesByGenreId(1);
    }
}