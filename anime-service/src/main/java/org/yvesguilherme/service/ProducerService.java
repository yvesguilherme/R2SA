package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.exception.ProducerAlreadyExistsException;
import org.yvesguilherme.repository.ProducerRepository;
import org.yvesguilherme.util.enums.ProducerEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
  private final ProducerRepository producerRepository;

  public List<Producer> findAll(String name) {
    return name == null ? producerRepository.findAll() : producerRepository.findByNameIgnoreCase(name);
  }

  public Producer findByIdOrThrowNotFound(Long id) {
    return producerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ProducerEnum.NOT_FOUND.getMessage()));
  }

  public Producer save(Producer producer) {
    assertProducerDoesNotExist(producer);
    return producerRepository.save(producer);
  }

  public void delete(Long id) {
    var producer = findByIdOrThrowNotFound(id);

    producerRepository.delete(producer);
  }

  public void update(Producer producerToUpdate) {
    findByIdOrThrowNotFound(producerToUpdate.getId());
    assertProducerDoesNotExist(producerToUpdate);
    producerRepository.save(producerToUpdate);
  }

  private void assertProducerDoesNotExist(Producer producer) {
    producerRepository.findByNameEqualsIgnoreCase(producer.getName()).ifPresent(this::throwProducerExistsException);
  }

  private void throwProducerExistsException(Producer producer) {
    throw new ProducerAlreadyExistsException("Producer with name %s already exists".formatted(producer.getName()));
  }
}
