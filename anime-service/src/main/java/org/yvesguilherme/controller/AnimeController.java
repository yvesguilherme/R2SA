package org.yvesguilherme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("animes")
public class AnimeController {

  @GetMapping()
  public ResponseEntity<List<String>> listAll() {
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
