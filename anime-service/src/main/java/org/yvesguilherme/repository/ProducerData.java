package org.yvesguilherme.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ProducerData {
  private final List<Producer> producers = new ArrayList<>();

  {
    var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
    var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
    var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();

    producers.addAll(List.of(mappa, kyotoAnimation, madhouse));
  }
}
