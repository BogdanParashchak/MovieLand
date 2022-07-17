package com.parashchak.movieland.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Genres not found")
public class GenresNotFoundException extends RuntimeException {
}