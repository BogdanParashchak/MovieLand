package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Movie;
import com.parashchak.movieland.exception.MoviesNotFoundException;
import com.parashchak.movieland.service.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieServiceImpl movieServiceImpl;

    @Test
    public void givenMovies_whenGetMovies_thenMoviesReturned() throws Exception {

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

        when(movieServiceImpl.findAll()).thenReturn(List.of(firstMockMovie, secondMockMovie));

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
                .andExpect(jsonPath("$.[1].rating").value(7.6));

        verify(movieServiceImpl).findAll();
    }

    @Test
    public void givenNoMovies_whenGetMovies_thenNotFoundStatusReturned() throws Exception {

        when(movieServiceImpl.findAll()).thenThrow(new MoviesNotFoundException());

        mockMvc.perform(get("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(status().reason("Movies not found"));

        verify(movieServiceImpl).findAll();
    }
}