package org.yvesguilherme.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.mapper.ProducerMapper;
import org.yvesguilherme.request.ProducerPostRequest;
import org.yvesguilherme.request.ProducerPutRequest;
import org.yvesguilherme.response.ProducerGetResponse;
import org.yvesguilherme.response.ProducerPostResponse;
import org.yvesguilherme.service.ProducerService;

import java.util.List;

@RestController
@RequestMapping("producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {
  private final ProducerMapper producerMapper;
  private final ProducerService producerService;

  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
    log.debug("Request to list all producers, param name: '{}'", name);

    var producers = producerService.findAll(name);
    var producerGetResponseList = producerMapper.toProducerGetResponseList(producers);

    return ResponseEntity.ok(producerGetResponseList);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find producer by id: {}", id);

    var producer = producerService.findByIdOrThrowNotFound(id);
    var producerGetResponse = producerMapper.toProducerGetResponse(producer);

    return ResponseEntity.ok(producerGetResponse);
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE,
          headers = "x-api-key=1234"
  )
  public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders httpHeaders) {
    log.info("{}", httpHeaders);

    var producer = producerMapper.toProducer(producerPostRequest);
    var producerSaved = producerService.save(producer);
    var producerPostResponse = producerMapper.toProducerPostResponse(producerSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete producer by id: {}", id);

    producerService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest producerPutRequest) {
    log.debug("Request to update producer: {}", producerPutRequest);

    var producerToUpdate = producerMapper.toProducer(producerPutRequest);

    producerService.update(producerToUpdate);

    return ResponseEntity.noContent().build();
  }

}
