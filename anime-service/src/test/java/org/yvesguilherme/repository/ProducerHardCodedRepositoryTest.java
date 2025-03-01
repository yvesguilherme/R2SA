package org.yvesguilherme.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use esta anotação quando você quer usar as funcionalidades do Spring.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {

  @InjectMocks
  private ProducerHardCodedRepository repository;

  @Mock
  private ProducerData producerData;

  private final List<Producer> producerList = new ArrayList<>();

  @BeforeEach
  void init() {
    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
    var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();

    producerList.addAll(List.of(ufotable, witStudio, studioGhibli));
  }


  @Test
  @DisplayName("findAll returns a list with all producers")
  @Order(1)
  void findAllReturnsAllProducersWhenSuccessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producers = repository.findAll();

    Assertions
            .assertThat(producers)
            .isNotNull()
            .isNotEmpty()
            .hasSameElementsAs(producerList);
  }

  @Test
  @DisplayName("findById returns a producer with given id")
  void findByIdReturnsProducerByIdWhenSuccessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var expectedProducer = producerList.getFirst();

    var producers = repository.findById(expectedProducer.getId());

    Assertions
            .assertThat(producers)
            .isPresent()
            .contains(expectedProducer);
  }

  @Test
  @DisplayName("findByName returns empty list when name is null")
  void findByNameReturnsEmptyListWhenNameIsNull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producers = repository.findByName(null);

    Assertions
            .assertThat(producers)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findByName returns a list with found object when name exists")
  void findByNameReturnsAListWithFoundObjectWhenNameExists() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var expectedProducer = producerData.getProducers().getFirst();
    var producers = repository.findByName(expectedProducer.getName());

    Assertions
            .assertThat(producers)
            .contains(expectedProducer);
  }

  @Test
  @DisplayName("save creates a producer")
  void saveCreatesAProducerWhenSuccessful() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producerToSave = Producer.builder().id(99L).name("Mappa").createdAt(LocalDateTime.now()).build();

    var producer = repository.save(producerToSave);

    Assertions
            .assertThat(producer)
            .isEqualTo(producerToSave)
            .hasNoNullFieldsOrProperties();

    Optional<Producer> producerSavedOptional = repository.findById(producerToSave.getId());

    Assertions
            .assertThat(producerSavedOptional)
            .isPresent()
            .contains(producerToSave);
  }

  @Test
  @DisplayName("delete removes a producer")
  void deleteRemovesProducerWhenSuccessful() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producerToDelete = producerData.getProducers().getFirst();
    repository.delete(producerToDelete);

    var producers = repository.findAll();

    Assertions
            .assertThat(producers)
            .isNotEmpty()
            .doesNotContain(producerToDelete);
  }

  @Test
  @DisplayName("update updates a producer")
  void updateUpdatesAProducerWhenSuccessful() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
    var producerToUpdate = this.producerList.getFirst();
    producerToUpdate.setName("Aniplex");

    repository.update(producerToUpdate);

    Assertions
            .assertThat(this.producerList)
            .contains(producerToUpdate);

    var producerUpdatedOptional = repository.findById(producerToUpdate.getId());

    Assertions
            .assertThat(producerUpdatedOptional)
            .isPresent();

    Assertions
            .assertThat(producerUpdatedOptional.get().getName())
            .isEqualTo(producerToUpdate.getName());
  }
}