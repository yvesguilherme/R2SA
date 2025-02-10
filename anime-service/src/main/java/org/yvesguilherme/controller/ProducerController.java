package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.request.ProducerPostRequest;
import org.yvesguilherme.response.ProducerGetResponse;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("producers")
@Slf4j
public class ProducerController {
  @GetMapping
  public ResponseEntity<List<Producer>> listAll(@RequestParam(required = false) String name) {
    if (name == null) {
      return new ResponseEntity<>(Producer.getProducers(), HttpStatus.OK);
    }

    List<Producer> listOfProducers = Producer
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
  public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders httpHeaders) {
    log.info("{}", httpHeaders);

    if (producerPostRequest.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

    var producer = Producer
            .builder()
            .id(ThreadLocalRandom.current().nextLong(100_000))
            .name(producerPostRequest.getName())
            .createdAt(LocalDateTime.now())
            .build();

    Producer.getProducers().add(producer);

    var producerGetResponse = ProducerGetResponse
            .builder()
            .id(producer.getId())
            .name(producer.getName())
            .createdAt(producer.getCreatedAt())
            .build();

    return new ResponseEntity<>(producerGetResponse, HttpStatus.CREATED);
  }

}
