package org.yvesguilherme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AnimeAlreadyExistsException extends ResponseStatusException {
  public AnimeAlreadyExistsException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
