package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.ProducerHardCodedRepository;
import org.yvesguilherme.util.enums.ProducerEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
  private final ProducerHardCodedRepository producerHardCodedRepository;

  public List<Producer> findAll(String name) {
    return name == null ? producerHardCodedRepository.findAll() : producerHardCodedRepository.findByName(name);
  }

  public Producer findByIdOrThrowNotFound(Long id) {
    return producerHardCodedRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ProducerEnum.NOT_FOUND.getMessage()));
  }

  public Producer save(Producer producer) {
    return producerHardCodedRepository.save(producer);
  }

  public void delete(Long id) {
    var producer = findByIdOrThrowNotFound(id);

    producerHardCodedRepository.delete(producer);
  }

  public void update(Producer producerToUpdate) {
    var producer = findByIdOrThrowNotFound(producerToUpdate.getId());

    producerToUpdate.setCreatedAt(producer.getCreatedAt());

    producerHardCodedRepository.update(producerToUpdate);
  }
}
