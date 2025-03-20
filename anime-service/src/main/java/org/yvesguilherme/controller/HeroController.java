package org.yvesguilherme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.util.Constants;
import org.yvesguilherme.util.enums.HeroEnum;

import java.util.List;

@RestController
@RequestMapping("heroes")
@RequiredArgsConstructor
public class HeroController {

  private final Constants constants;

  @GetMapping
  public List<String> findAll() {
    return constants.getListHeroes();
  }

  @GetMapping("filter")
  public List<String> listAllHeroesParam(@RequestParam(defaultValue = "") String name) {
    return constants.getListHeroes().stream().filter(hero -> hero.equalsIgnoreCase(name)).toList();
  }

  @GetMapping("filterList")
  public List<String> listAllHeroesParamList(@RequestParam(defaultValue = "") List<String> names) {
    return constants.getListHeroes().stream().filter(names::contains).toList();
  }

  @GetMapping("{name}")
  public String findByName(@PathVariable String name) {
    return constants.getListHeroes()
            .stream()
            .filter(hero -> hero.equalsIgnoreCase(name))
            .findFirst()
            .orElse(HeroEnum.NOT_FOUND.getMessage());
  }
}
