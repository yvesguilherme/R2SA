package org.yvesguilherme.repository;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {
  private static final List<Producer> PRODUCERS = new ArrayList<>();

  @Qualifier("connectionMongoDB")
  private final Connection connectionMongoDB;

  static {
    var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
    var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
    var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();

    PRODUCERS.addAll(List.of(mappa, kyotoAnimation, madhouse));
  }

  public List<Producer> findAll() {
    log.debug(connectionMongoDB);
    return PRODUCERS;
  }

  public Optional<Producer> findById(Long id) {
    return PRODUCERS
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst();
  }

  public List<Producer> findByName(String name) {
    return PRODUCERS
            .stream()
            .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
  }

  public Producer save(Producer producer) {
    PRODUCERS.add(producer);

    return producer;
  }

  public void update(Producer producer) {
    delete(producer);
    save(producer);
  }

  public void delete(Producer producer) {
    PRODUCERS.removeIf(p -> p.getId().equals(producer.getId()));
  }
}
