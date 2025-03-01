package org.yvesguilherme.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.Anime;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class AnimeData {
  private final List<Anime> animeList = new ArrayList<>();

  {
    var ninja = Anime.builder().name("Ninja Kamui").id(1L).build();
    var kaijuu = Anime.builder().name("Kaijuu-8gou").id(2L).build();
    var haikyuu = Anime.builder().name("Haikyuu!! Second Season").id(3L).build();
    var hunter = Anime.builder().name("Hunter x Hunter (2011)").id(4L).build();
    var jujutsu = Anime.builder().name("Jujutsu Kaisen").id(5L).build();
    var gintama = Anime.builder().name("Gintama': Enchousen").id(6L).build();
    var demon = Anime.builder().name("Demon Slayer: Kimetsu no Yaiba").id(7L).build();

    animeList.addAll(List.of(ninja, kaijuu, haikyuu, hunter, jujutsu, gintama, demon));
  }
}
