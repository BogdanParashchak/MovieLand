package com.parashchak.movieland.web;

import com.parashchak.movieland.entity.Genre;
import com.parashchak.movieland.exception.GenresNotFoundException;
import com.parashchak.movieland.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    public void givenGenres_whenGetGenres_thenGenresReturned() throws Exception {

        Genre firstMockGenre = Genre.builder()
                .id(1)
                .name("first_genre")
                .build();

        Genre secondMockGenre = Genre.builder()
                .id(2)
                .name("second_genre")
                .build();

        Genre thirdMockGenre = Genre.builder()
                .id(3)
                .name("second_genre")
                .build();

        when(genreService.findAll()).thenReturn(List.of(firstMockGenre, secondMockGenre, thirdMockGenre));

        mockMvc.perform(get("/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("first_genre"))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("second_genre"))

                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].name").value("second_genre"));

        verify(genreService).findAll();
    }

    @Test
    public void givenNoGenres_whenGetGenres_thenNotFoundStatusReturned() throws Exception {

        when(genreService.findAll()).thenThrow(new GenresNotFoundException());

        mockMvc.perform(get("/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isNotFound())
                .andExpect(status().reason("Genres not found"));

        verify(genreService).findAll();
    }
}