package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.mapper.AnimeMapper;
import org.yvesguilherme.request.AnimePostRequest;
import org.yvesguilherme.response.AnimeGetResponse;
import org.yvesguilherme.response.AnimePostResponse;
import org.yvesguilherme.request.AnimePutRequest;
import org.yvesguilherme.util.Constants;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {
  private static final AnimeMapper ANIME_MAPPER = AnimeMapper.INSTANCE;

  @GetMapping("thread-test")
  public ResponseEntity<List<String>> listAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return new ResponseEntity<>(Constants.LIST_OF_ANIME, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> listOfAnime(@RequestParam(required = false) String name) {
    log.debug("Request received to list all animes, param name '{}'", name);

    List<AnimeGetResponse> listOfAnimeGetResponse = ANIME_MAPPER.toAnimeGetResponseList(Anime.getAnimes());

    if (name == null) {
      return new ResponseEntity<>(listOfAnimeGetResponse, HttpStatus.OK);
    }

    List<AnimeGetResponse> animeGetResponseList = listOfAnimeGetResponse
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    return new ResponseEntity<>(animeGetResponseList, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find anime by id: {}", id);

    return Anime
            .getAnimes()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .map(ANIME_MAPPER::toAnimeGetResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
    log.debug("Request to save anime: {}", animePostRequest);

    if (animePostRequest.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

    var anime = ANIME_MAPPER.toAnime(animePostRequest);

    Anime.getAnimes().add(anime);

    var animeResponse = ANIME_MAPPER.toAnimePostResponse(anime);

    return new ResponseEntity<>(animeResponse, HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete anime by id: {}", id);

    Anime animeToDelete = Anime
            .getAnimes()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));

    Anime.getAnimes().remove(animeToDelete);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
    log.debug("Request to update anime: {}", animePutRequest);

    Anime animeToRemove = Anime
            .getAnimes()
            .stream()
            .filter(a -> a.getId().equals(animePutRequest.getId()))
            .findFirst()
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));

    Anime animeUpdated = ANIME_MAPPER.toAnime(animePutRequest);
    Anime.getAnimes().remove(animeToRemove);
    Anime.getAnimes().add(animeUpdated);

    return ResponseEntity.noContent().build();
  }

}
