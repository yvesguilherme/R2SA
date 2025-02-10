package org.yvesguilherme.domain;

import lombok.*;
import org.yvesguilherme.util.Constants;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Anime {
  private Long id;

  @NonNull
  private String name;

  @Getter
  private static List<Anime> animes = new ArrayList<>();

  static {
    for (int x = 0; x < Constants.LIST_OF_ANIME.size(); x++) {
      animes.add(
              new Anime(x + 1L, Constants.LIST_OF_ANIME.get(x))
      );
    }
  }
}



