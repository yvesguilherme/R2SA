package org.yvesguilherme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProducerAlreadyExistsException extends ResponseStatusException {
  public ProducerAlreadyExistsException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
