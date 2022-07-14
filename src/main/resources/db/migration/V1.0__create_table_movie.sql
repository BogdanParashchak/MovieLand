CREATE TABLE "movie"
(
    "id"              SERIAL PRIMARY KEY,
    "name_translated" VARCHAR(255)  NOT NULL,
    "name_original"   VARCHAR(255)  NOT NULL,
    "release_date"    DATE          NOT NULL,
    "description"     VARCHAR(1000) NOT NULL,
    "price"           DECIMAL(8, 2) NOT NULL,
    "picture_path"    VARCHAR(255)  NOT NULL,
    "rating"          DECIMAL(8, 2) NOT NULL,
    "votes"           INTEGER       NOT NULL
);