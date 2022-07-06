CREATE TABLE "movie"
(
    "id"              INTEGER       NOT NULL,
    "name_translated" VARCHAR(255)  NOT NULL,
    "name_original"   VARCHAR(255)  NOT NULL,
    "release_date"    DATE          NOT NULL,
    "description"     VARCHAR(1000) NOT NULL,
    "price"           DECIMAL(8, 2) NOT NULL,
    "picture_path"    VARCHAR(255)  NOT NULL,
    "rating"          DECIMAL(8, 2) NOT NULL,
    "votes"           INTEGER       NOT NULL
);
ALTER TABLE
    "movie"
    ADD PRIMARY KEY ("id");

CREATE TABLE "genre"
(
    "id"   INTEGER     NOT NULL,
    "name" VARCHAR(50) NOT NULL
);
ALTER TABLE
    "genre"
    ADD PRIMARY KEY ("id");

CREATE TABLE "movie_genre"
(
    "movie_id" INTEGER NOT NULL,
    "genre_id" INTEGER NOT NULL
);
ALTER TABLE
    "movie_genre"
    ADD PRIMARY KEY ("movie_id", "genre_id");