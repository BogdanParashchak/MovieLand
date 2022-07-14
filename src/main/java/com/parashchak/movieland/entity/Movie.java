package com.parashchak.movieland.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name="name_translated", nullable = false)
    private String translatedName;

    @Column(name="name_original", nullable = false)
    private String originalName;

    @Column(name="release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="price", nullable = false)
    private Double price;

    @Column(name="picture_path", nullable = false)
    private String picturePath;

    @Column(name="rating", nullable = false)
    private Double rating;

    @Column(name="votes", nullable = false)
    private int votes;
}
