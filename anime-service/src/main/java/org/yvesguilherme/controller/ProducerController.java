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
import org.yvesguilherme.service.ProducerService;
import org.yvesguilherme.util.enums.AnimeEnum;
import org.yvesguilherme.util.enums.ProducerEnum;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("producers")
@Slf4j
public class ProducerController {
  private static final ProducerMapper PRODUCER_MAPPER = ProducerMapper.INSTANCE;
  private ProducerService producerService;

  private ProducerController(){
    this.producerService = new ProducerService();
  }

  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
    log.debug("Request to list all producers, param name: '{}'", name);

    var producers = producerService.findAll(name);
    var producerGetResponseList = PRODUCER_MAPPER.toProducerGetResponseList(producers);

    return ResponseEntity.ok(producerGetResponseList);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find producer by id: {}", id);

    var producer = producerService.findByIdOrThrowNotFound(id);
    var producerGetResponse = PRODUCER_MAPPER.toProducerGetResponse(producer);

    return ResponseEntity.ok(producerGetResponse);
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE,
          headers = "x-api-key=1234"
  )
  public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders httpHeaders) {
    log.info("{}", httpHeaders);

    var producer = PRODUCER_MAPPER.toProducer(producerPostRequest);
    var producerSaved = producerService.save(producer);
    var producerGetResponse = PRODUCER_MAPPER.toProducerGetResponse(producerSaved);

    return ResponseEntity.ok(producerGetResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete producer by id: {}", id);

    producerService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {
    log.debug("Request to update producer: {}", producerPutRequest);

    var producerToUpdate = PRODUCER_MAPPER.toProducer(producerPutRequest);

    producerService.update(producerToUpdate);

    return ResponseEntity.noContent().build();
  }

}
