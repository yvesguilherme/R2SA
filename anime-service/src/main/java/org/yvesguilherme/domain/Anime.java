package org.yvesguilherme.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.yvesguilherme.util.Constants.LIST_OF_ANIME;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Anime {
  private Long id;

  @NonNull
  private String name;


  public static List<Anime> listOfAnime() {
    List<Anime> animes = new ArrayList<>();

    for (int x = 0; x < LIST_OF_ANIME.size(); x++) {
      animes.add(
              new Anime(x + 1L, LIST_OF_ANIME.get(x))
      );
    }

    return animes;
  }

  public static List<Anime> listOfAnimeWithRandomId() {
    return LIST_OF_ANIME
            .stream()
            .map(anime -> new Anime(ThreadLocalRandom.current().nextLong(1, 100), anime))
            .toList();
  }
}



