package org.yvesguilherme.exception.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(AnimeNotFoundException.class)
  public ResponseEntity<String> handleAnimeNotFound(AnimeNotFoundException animeNotFoundException) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(animeNotFoundException.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequest(BadRequestException badRequestException) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(badRequestException.getMessage());
  }
}
