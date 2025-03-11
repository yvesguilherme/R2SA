package org.yvesguilherme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
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

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  @Autowired
  private ResourceLoader resourceLoader;

  @BeforeEach
  void init() {
    var localDateTime = createMockLocalDateTime();

    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
    var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();

    producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
  }

  @Test
  @DisplayName("findAll returns a list with all Producers when argument is null")
  void findAll_ReturnsAListWithAllProducers_WhenArgumentIsNull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = readResourceFile("producer/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get("/producers"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  private static LocalDateTime createMockLocalDateTime() {
    var dateTime = "2025-03-11T11:04:13.658154042";
    var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }

  private String readResourceFile(String fileName) throws IOException {
    var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();

    return new String(Files.readAllBytes(file.toPath()));
  }
}