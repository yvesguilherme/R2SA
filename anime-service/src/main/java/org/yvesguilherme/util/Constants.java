package org.yvesguilherme.util;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
public class Constants {
  private final List<String> listAnime = new ArrayList<>();
  private final List<String> listHeroes = new ArrayList<>();

  {
    listAnime.addAll(
            List.of(
                    "Ninja Kamui",
                    "Kaijuu-8gou",
                    "Haikyuu!! Second Season",
                    "Hunter x Hunter (2011)",
                    "Jujutsu Kaisen",
                    "Gintama': Enchousen",
                    "Demon Slayer: Kimetsu no Yaiba"
            )
    );

    listHeroes.addAll(List.of("Guts", "Zoro", "Kakashi", "Goku"));
  }
}
