package com.parashchak.movieland.service;

import com.parashchak.movieland.entity.Genre;
import com.parashchak.movieland.exception.GenresNotFoundException;
import com.parashchak.movieland.repository.GenreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(PER_CLASS)
class GenreServiceImplTest {

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    private final List<Genre> expectedGenres = new ArrayList<>();

    @BeforeAll
    void setUp() {
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

        expectedGenres.add(firstMockGenre);
        expectedGenres.add(secondMockGenre);
        expectedGenres.add(thirdMockGenre);
    }

    @Test
    void givenListOfGenres_whenFindAll_thenListOfGenresReturned() {

        //prepare
        Mockito.when(genreRepository.findAll()).thenReturn(expectedGenres);

        //when
        List<Genre> actualGenres = genreService.findAllGenres();

        //then
        assertEquals(expectedGenres, actualGenres);
        verify(genreRepository).findAll();
    }

    @Test
    void givenEmptyListOfGenres_whenFindAll_thenGenresNotFoundExceptionThrown() {

        //prepare
        Mockito.when(genreRepository.findAll()).thenReturn(new ArrayList<>());

        //then
        assertThrows(GenresNotFoundException.class, () -> genreService.findAllGenres());
        verify(genreRepository).findAll();
    }
}