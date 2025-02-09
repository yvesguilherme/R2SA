package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.AnimeNotFoundException;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.yvesguilherme.util.Constants.LIST_OF_ANIME;

@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {
  @GetMapping("thread-test")
  public ResponseEntity<List<String>> listAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return new ResponseEntity<>(LIST_OF_ANIME, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Anime>> listOfAnime(@RequestParam(required = false) String name) {
    if (name == null) {
      return new ResponseEntity<>(Anime.listOfAnime(), HttpStatus.OK);
    }

    List<Anime> listOfAnime = Anime
            .listOfAnime()
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();

    return new ResponseEntity<>(listOfAnime, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Anime> findById(@PathVariable Long id) {
    return Anime
            .listOfAnime()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new AnimeNotFoundException(AnimeEnum.NOT_FOUND.getMessage()));
  }
}
