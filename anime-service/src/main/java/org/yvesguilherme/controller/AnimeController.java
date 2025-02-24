package org.yvesguilherme.controller;

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
public class AnimeController {
  private static final AnimeMapper ANIME_MAPPER = AnimeMapper.INSTANCE;
  private final AnimeService animeService;

  private AnimeController() {
    this.animeService = new AnimeService();
  }

  @GetMapping("thread-test")
  public ResponseEntity<List<String>> listAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return new ResponseEntity<>(Constants.LIST_OF_ANIME, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> listOfAnime(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    var listOfAnime = animeService.findAll(name);
    var listOfAnimeGetResponse = ANIME_MAPPER.toAnimeGetResponseList(listOfAnime);

    return ResponseEntity.ok(listOfAnimeGetResponse);
  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find anime by id: {}", id);

    var anime = animeService.findByIdOrThrowNotFound(id);
    var animeGetResponse = ANIME_MAPPER.toAnimeGetResponse(anime);

    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
    log.debug("Request to save anime: {}", animePostRequest);

    var anime = ANIME_MAPPER.toAnime(animePostRequest);
    var animeSaved = animeService.save(anime);
    var animePostResponse = ANIME_MAPPER.toAnimePostResponse(animeSaved);

    return ResponseEntity.ok(animePostResponse);
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

    var animeUpdated = ANIME_MAPPER.toAnime(animePutRequest);

    animeService.update(animeUpdated);

    return ResponseEntity.noContent().build();
  }

}
