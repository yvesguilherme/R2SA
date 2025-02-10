package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.util.Constants;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {
  @GetMapping("thread-test")
  public ResponseEntity<List<String>> listAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return new ResponseEntity<>(Constants.LIST_OF_ANIME, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Anime>> listOfAnime(@RequestParam(required = false) String name) {
    if (name == null) {
      return new ResponseEntity<>(Anime.getAnimes(), HttpStatus.OK);
    }

    List<Anime> listOfAnime = Anime
            .getAnimes()
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    return new ResponseEntity<>(listOfAnime, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Anime> findById(@PathVariable Long id) {
    return Anime
            .getAnimes()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));
  }

  @PostMapping
  public ResponseEntity<Anime> save(@RequestBody Anime anime) {
    if (anime.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

    anime.setId(ThreadLocalRandom.current().nextLong(100_000));
    Anime.getAnimes().add(anime);

    return new ResponseEntity<>(anime, HttpStatus.OK);
  }

}
