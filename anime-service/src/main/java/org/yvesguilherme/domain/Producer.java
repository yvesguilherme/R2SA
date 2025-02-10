package org.yvesguilherme.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.yvesguilherme.util.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Producer {
  private Long id;

  @NonNull
  @JsonProperty("name")
  private String name;

  private LocalDateTime createdAt;

  @Getter
  private static List<Producer> producers = new ArrayList<>();

  static {
    var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
    var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
    var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();

    producers.addAll(List.of(mappa, kyotoAnimation, madhouse));
  }

}



