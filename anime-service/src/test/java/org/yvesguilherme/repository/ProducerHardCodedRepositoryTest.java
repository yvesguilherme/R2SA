package org.yvesguilherme.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Use esta anotação quando você quer usar as funcionalidades do Spring.
 */
@ExtendWith(MockitoExtension.class)
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
  void findAllReturnsAllProducersWhenSuccessfull() {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

    var producers = repository.findAll();

    Assertions
            .assertThat(producers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(producerList.size());
  }
}