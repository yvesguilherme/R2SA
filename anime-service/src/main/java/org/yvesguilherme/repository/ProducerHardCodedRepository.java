package org.yvesguilherme.repository;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Producer;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {
  private final ProducerData producerData;

  @Qualifier("connectionMongoDB")
  private final Connection connectionMongoDB;


  public List<Producer> findAll() {
    log.debug(connectionMongoDB);
    return producerData.getProducers();
  }

  public Optional<Producer> findById(Long id) {
    return producerData.getProducers()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst();
  }

  public List<Producer> findByName(String name) {
    return producerData.getProducers()
            .stream()
            .filter(a -> a.getName().equalsIgnoreCase(name))
            .toList();
  }

  public Producer save(Producer producer) {
    producerData.getProducers().add(producer);

    return producer;
  }

  public void update(Producer producer) {
    delete(producer);
    save(producer);
  }

  public void delete(Producer producer) {
    producerData.getProducers().removeIf(p -> p.getId().equals(producer.getId()));
  }
}
