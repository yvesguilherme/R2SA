package org.yvesguilherme.controller;

import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.util.enums.HeroEnum;

import java.util.List;

@RestController
@RequestMapping("heroes")
public class HeroController {
  private static final List<String> HEROES = List.of("Guts", "Zoro", "Kakashi", "Goku");

  @GetMapping
  public List<String> listAllHeroes() {
    return HEROES;
  }

  @GetMapping("filter")
  public List<String> listAllHeroesParam(@RequestParam(defaultValue = "") String name) {
    return HEROES.stream().filter(hero -> hero.equalsIgnoreCase(name)).toList();
  }

  @GetMapping("filterList")
  public List<String> listAllHeroesParamList(@RequestParam(defaultValue = "") List<String> names) {
    return HEROES.stream().filter(names::contains).toList();
  }

  @GetMapping("{name}")
  public String findByName(@PathVariable String name) {
    return HEROES
            .stream()
            .filter(hero -> hero.equalsIgnoreCase(name))
            .findFirst()
            .orElse(HeroEnum.NOT_FOUND.getMessage());
  }
}
