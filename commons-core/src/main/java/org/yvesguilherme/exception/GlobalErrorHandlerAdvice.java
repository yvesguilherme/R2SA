package org.yvesguilherme.exception;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
  // https://www.postgresql.org/docs/current/errcodes-appendix.html
  private static final String UNIQUE_VIOLATION = "23505";

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e) {
    var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(PSQLException.class)
  public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityConstraintViolationException(PSQLException e) {
    if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
      var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new DefaultErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database error"));
  }
}
