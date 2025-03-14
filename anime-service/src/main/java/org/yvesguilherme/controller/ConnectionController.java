package org.yvesguilherme.controller;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connections")
@Slf4j
@RequiredArgsConstructor
public class ConnectionController {
  private final Connection connectionPostgreSQL;

  @GetMapping
  public ResponseEntity<Connection> getConnection() {
    log.info("Getting connection");
    return ResponseEntity.ok(connectionPostgreSQL);
  }
}
