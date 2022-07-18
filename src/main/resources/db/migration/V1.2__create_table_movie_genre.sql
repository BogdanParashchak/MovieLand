CREATE TABLE "movie_genre"
(
    "id"       SERIAL PRIMARY KEY,
    "movie_id" INTEGER NOT NULL,
    "genre_id" INTEGER NOT NULL
);

ALTER TABLE "movie_genre"
    ADD CONSTRAINT "movie_genre_movie_id_foreign" FOREIGN KEY ("movie_id") REFERENCES "movie" ("id");
ALTER TABLE "movie_genre"
    ADD CONSTRAINT "movie_genre_genre_id_foreign" FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");