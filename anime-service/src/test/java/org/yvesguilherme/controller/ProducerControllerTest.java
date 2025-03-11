package org.yvesguilherme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.config.ConnectionConfiguration;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.mapper.ProducerMapperImpl;
import org.yvesguilherme.repository.ProducerData;
import org.yvesguilherme.repository.ProducerHardCodedRepository;
import org.yvesguilherme.service.ProducerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)
@Import({
        ConnectionConfiguration.class,
        ProducerData.class,
        ProducerHardCodedRepository.class,
        ProducerMapperImpl.class,
        ProducerService.class
})
//@ComponentScan(basePackages = "org.yvesguilherme")
class ProducerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProducerData producerData;

  private List<Producer> producerList;

  @BeforeEach
  void init() {
    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
    var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();

    producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
  }

  @Test
  @DisplayName("findAll returns a list with all Producers when argument is null")
  void findAll_ReturnsAListWithAllProducers_WhenArgumentIsNull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    mockMvc.perform(MockMvcRequestBuilders.get("/producers"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
  }
}