package org.yvesguilherme.domain;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
  @EqualsAndHashCode.Include
  private Long id;

  @NonNull
  private String name;
}



