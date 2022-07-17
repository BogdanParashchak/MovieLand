package com.parashchak.movieland.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genre")
public class Genre {

    @Id
    @SequenceGenerator(
            name = "genre_sequence_generator",
            sequenceName = "genre_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "genre_sequence_generator")
    private Integer id;

    @Column(nullable = false)
    private String name;
}