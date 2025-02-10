package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.util.Constants;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("producers")
@Slf4j
public class ProducerController {
  @GetMapping
  public ResponseEntity<List<Producer>> listAll(@RequestParam(required = false) String name) {
    if (name == null) {
      return new ResponseEntity<>(Producer.getProducers(), HttpStatus.OK);
    }

    List<Producer> listOfProducers= Producer
            .getProducers()
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    return new ResponseEntity<>(listOfProducers, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Producer> findById(@PathVariable Long id) {
    return Producer
            .getProducers()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE,
          headers = "x-api-key=1234"
  )
  public ResponseEntity<Producer> save(@RequestBody Producer producer, @RequestHeader HttpHeaders httpHeaders) {
    log.info("{}", httpHeaders);

    if (producer.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

    producer.setId(ThreadLocalRandom.current().nextLong(100_000));
    Producer.getProducers().add(producer);

    return new ResponseEntity<>(producer, HttpStatus.OK);
  }

}
