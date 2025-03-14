package org.yvesguilherme.commons;

import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.Anime;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {
  public List<Anime> newAnimeList() {
    var attackOnTitan = Anime.builder().name("Attack on Titan").id(1L).build();
    var demonSlayer = Anime.builder().name("Demon Slayer").id(2L).build();
    var myHeroAcademia = Anime.builder().name("My Hero Academia").id(3L).build();

    return new ArrayList<>(List.of(attackOnTitan, demonSlayer, myHeroAcademia));
  }

  public Anime newAnimeToSave(String name) {
    var nameToSave = name == null ? "Naruto" : name;
    return Anime.builder().id(999L).name(nameToSave).build();
  }
}
