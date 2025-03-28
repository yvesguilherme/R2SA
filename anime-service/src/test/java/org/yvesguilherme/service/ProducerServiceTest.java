package org.yvesguilherme.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.commons.ProducerUtils;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.ProducerHardCodedRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

  @InjectMocks
  private ProducerService service;

  @Mock
  private ProducerHardCodedRepository repository;

  private List<Producer> producerList;

  @InjectMocks
  private ProducerUtils producerUtils;

  @BeforeEach
  void init() {
    producerList = producerUtils.newProducerList();
  }

  @Test
  @DisplayName("findAll returns a list with all producers when argument is null")
  @Order(1)
  void findAllReturnsAllProducersWhenArgumentIsNull() {
    BDDMockito.when(repository.findAll()).thenReturn(producerList);

    var producers = service.findAll(null);

    Assertions.assertThat(producers).isNotNull().isNotEmpty().hasSameElementsAs(producerList);
  }

  @Test
  @DisplayName("findAll returns a list with all producers when argument exists")
  @Order(2)
  void findAllReturnsAllProducersWhenArgumentExists() {
    var producer = producerList.getFirst();
    var expectedProducersFound = singletonList(producer);

    BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducersFound);

    var producersFound = service.findAll(producer.getName());

    Assertions.assertThat(producersFound).containsAll(expectedProducersFound);
  }

  @Test
  @DisplayName("findAll returns empty list when name is not found")
  @Order(3)
  void findAllReturnsEmptyListWhenNameIsNotFound() {
    var producer = "not-found";

    BDDMockito.when(repository.findByName(producer)).thenReturn(emptyList());

    var producersFound = service.findAll(producer);

    Assertions.assertThat(producersFound).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns a producer with given id")
  @Order(4)
  void findByIdReturnsProducerByIdWhenSuccessful() {
    var expectedProducer = producerList.getFirst();

    BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

    var producer = service.findByIdOrThrowNotFound(expectedProducer.getId());

    Assertions.assertThat(producer).isEqualTo(expectedProducer);
  }

  @Test
  @DisplayName("findById throws NotFoundException when producer is not found")
  @Order(5)
  void findByIdThrowsNotFoundExceptionWhenProducerIsNotFound() {
    var expectedProducer = producerList.getFirst();

    BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("save creates a producer")
  @Order(6)
  void saveCreatesAProducerWhenSuccessful() {
    var producerToSave = producerUtils.newProducerToSave(null);

    BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

    var savedProducer = service.save(producerToSave);

    Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete removes a producer")
  @Order(8)
  void deleteRemovesProducerWhenSuccessful() {
    var producerToDelete = producerList.getFirst();

    BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
    BDDMockito.doNothing().when(repository).delete(producerToDelete);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.delete(producerToDelete.getId()));
  }

  @Test
  @DisplayName("delete throws NotFoundException when producer is not found")
  @Order(9)
  void deleteThrowsNotFoundExceptionWhenProducerIsNotFound() {
    var producerToDelete = producerList.getFirst();

    BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.delete(producerToDelete.getId()))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("update updates a producer")
  @Order(10)
  void updateUpdatesAProducerWhenSuccessful() {
    var producerToUpdate = producerList.getFirst();
    producerToUpdate.setName("Aniplex");

    BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
    BDDMockito.doNothing().when(repository).update(producerToUpdate);

    service.update(producerToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(producerToUpdate));
  }

  @Test
  @DisplayName("update throws NotFoundException when producer is not found")
  @Order(11)
  void updateThrowsNotFoundExceptionWhenProducerIsNotFound() {
    var producerToUpdate = producerList.getFirst();

    BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(producerToUpdate))
            .isInstanceOf(NotFoundException.class);
  }
}