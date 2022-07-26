package com.parashchak.movieland.repository;

import com.parashchak.movieland.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query("SELECT m FROM Movie m " +
           "JOIN MovieGenre mg ON m.id = mg.movie.id " +
           "JOIN Genre g ON mg.genre.id = g.id WHERE g.id = ?1")
    List<Movie> findMoviesByGenreId(int genreId, Pageable page);
}