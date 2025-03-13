package org.yvesguilherme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.mapper.AnimeMapper;
import org.yvesguilherme.request.AnimePostRequest;
import org.yvesguilherme.request.AnimePutRequest;
import org.yvesguilherme.response.AnimeGetResponse;
import org.yvesguilherme.response.AnimePostResponse;
import org.yvesguilherme.service.AnimeService;
import org.yvesguilherme.util.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {
  private final AnimeMapper animeMapper;
  private final AnimeService animeService;
  private final Constants constants;

  @GetMapping("thread-test")
  public ResponseEntity<List<String>> findAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return new ResponseEntity<>(constants.getListAnime(), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var listOfAnime = animeService.findAll(name);
    var listOfAnimeGetResponse = animeMapper.toAnimeGetResponseList(listOfAnime);

    return ResponseEntity.ok(listOfAnimeGetResponse);
  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find anime by id: {}", id);

    var anime = animeService.findByIdOrThrowNotFound(id);
    var animeGetResponse = animeMapper.toAnimeGetResponse(anime);

    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
    log.debug("Request to save anime: {}", animePostRequest);

    var anime = animeMapper.toAnime(animePostRequest);
    var animeSaved = animeService.save(anime);
    var animePostResponse = animeMapper.toAnimePostResponse(animeSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete anime by id: {}", id);

    animeService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
    log.debug("Request to update anime: {}", animePutRequest);

    var animeUpdated = animeMapper.toAnime(animePutRequest);

    animeService.update(animeUpdated);

    return ResponseEntity.noContent().build();
  }

}
