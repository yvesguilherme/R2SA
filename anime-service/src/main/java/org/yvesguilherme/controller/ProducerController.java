package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.mapper.ProducerMapper;
import org.yvesguilherme.request.AnimePutRequest;
import org.yvesguilherme.request.ProducerPostRequest;
import org.yvesguilherme.request.ProducerPutRequest;
import org.yvesguilherme.response.ProducerGetResponse;
import org.yvesguilherme.util.enums.AnimeEnum;
import org.yvesguilherme.util.enums.ProducerEnum;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("producers")
@Slf4j
public class ProducerController {
  private static final ProducerMapper PRODUCER_MAPPER = ProducerMapper.INSTANCE;

  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request to list all producers, param name: '{}'", name);

    List<ProducerGetResponse> listOfProducerGetResponse = PRODUCER_MAPPER.toProducerGetResponseList(Producer.getProducers());

    if (name == null) {
      return new ResponseEntity<>(listOfProducerGetResponse, HttpStatus.OK);
    }

    List<ProducerGetResponse> listOfProducers = listOfProducerGetResponse
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    return new ResponseEntity<>(listOfProducers, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find producer by id: {}", id);

    return Producer
            .getProducers()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .map(PRODUCER_MAPPER::toProducerGetResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ProducerEnum.NOT_FOUND.getMessage()));
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

    var producer = PRODUCER_MAPPER.toProducer(producerPostRequest);
    var producerGetResponse = PRODUCER_MAPPER.toProducerGetResponse(producer);

    Producer.getProducers().add(producer);

    return new ResponseEntity<>(producerGetResponse, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete producer by id: {}", id);

    Producer producerToDelete = Producer
            .getProducers()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ProducerEnum.NOT_FOUND.getMessage()));

    Producer.getProducers().remove(producerToDelete);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {
    log.debug("Request to update producer: {}", producerPutRequest);

    Producer animeToRemove = Producer
            .getProducers()
            .stream()
            .filter(a -> a.getId().equals(producerPutRequest.getId()))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ProducerEnum.NOT_FOUND.getMessage()));

    Producer producerUpdated = PRODUCER_MAPPER.toProducer(producerPutRequest);
    producerUpdated.setCreatedAt(LocalDateTime.now());
    Producer.getProducers().remove(animeToRemove);
    Producer.getProducers().add(producerUpdated);

    return ResponseEntity.noContent().build();
  }

}
