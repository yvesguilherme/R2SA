package org.yvesguilherme.util;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
public class Constants {
  private final List<String> listAnimeNames = new ArrayList<>();

  {
    listAnimeNames.addAll(
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
  }
}
