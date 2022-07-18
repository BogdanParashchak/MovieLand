package com.parashchak.movieland.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_genre")
public class MovieGenre {

    @Id
    @SequenceGenerator(
            name = "movie_genre_sequence_generator",
            sequenceName = "movie_genre_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_genre_sequence_generator")
    private Integer id;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;
}