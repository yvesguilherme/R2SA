package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.BadRequestException;
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ProducerEnum.NOT_FOUND.getMessage()));
  }

  public Producer save(Producer producer) {
    if (producer.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

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
