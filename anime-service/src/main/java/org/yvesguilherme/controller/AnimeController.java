package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {

  @GetMapping()
  public ResponseEntity<List<String>> listAll() throws InterruptedException {
    log.info(Thread.currentThread().getName());
//    TimeUnit.SECONDS.sleep(1);
    List<String> listOfAnimes = List.of(
            "Ninja Kamui",
            "Kaijuu-8gou",
            "Haikyuu!! Second Season",
            "Hunter x Hunter (2011)",
            "Jujutsu Kaisen",
            "Gintama': Enchousen",
            "Demon Slayer: Kimetsu no Yaiba"
    );

    return new ResponseEntity<>(listOfAnimes, HttpStatus.OK);
  }
}
