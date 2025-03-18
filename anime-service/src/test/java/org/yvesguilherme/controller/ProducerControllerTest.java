package org.yvesguilherme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.commons.ProducerUtils;
import org.yvesguilherme.config.ConnectionConfiguration;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.mapper.ProducerMapperImpl;
import org.yvesguilherme.repository.ProducerData;
import org.yvesguilherme.repository.ProducerHardCodedRepository;
import org.yvesguilherme.service.ProducerService;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)
@Import({
        ConnectionConfiguration.class,
        ProducerData.class,
        ProducerHardCodedRepository.class,
        ProducerMapperImpl.class,
        ProducerService.class,
        FileUtils.class,
        ProducerUtils.class
})
//@ComponentScan(basePackages = "org.yvesguilherme")
//@ActiveProfiles("test")
class ProducerControllerTest {
  private static final String URL = "/producers";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProducerData producerData;

  @MockitoSpyBean
  private ProducerHardCodedRepository repository;

  private List<Producer> producerList;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private ProducerUtils producerUtils;

  @BeforeEach
  void init() {
    producerList = producerUtils.newProducerList();
  }

  @Test
  @DisplayName("GET v1/producers returns a list with all Producers when argument is null")
  void findAll_ReturnsAListWithAllProducers_WhenArgumentIsNull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers?name=Ufotable returns a list with found object when name exists")
  void findAll_ReturnsAListWithFoundObject_WhenNameExists() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
    var name = "Ufotable";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers?name=x returns an empty list when name is not found")
  void findAll_ReturnsAnEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers/1 returns a Producer with given id")
  void findById_ReturnsAProducerWithGivenId_WhenSuccessful() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados

    var response = fileUtils.readResourceFile("producer/get-producer-by-id-1-200.json");
    var id = 1;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers/4 throws ResponseStatusException 404 when Producer is not found")
  void findById_ThrowsResponseStatusException404_WhenProducerIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados

    var id = 4;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
  }

  @Test
  @DisplayName("POST /producers creates a Producer when successful")
  void save_CreatesAProducer_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
    var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
    var producerToSave = producerUtils.newProducerToSave("Aniplex");

    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("x-api-key", "1234")
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("PUT /producers updates a Producer when successful")
  void update_UpdatesAProducer_WhenSuccessful() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("PUT /producers throws ResponseStatusException when producer is not found")
  void update_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados

    var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));

  }

  @Test
  @DisplayName("DELETE /producers/1 delete removes a producer")
  void delete_RemovesProducer_WhenSuccessful() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados

    var producerId = producerList.getFirst().getId();

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /producers/99 throws ResponseStatusException when producer is not found")
  void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producerList); // Não temos domínio sobre os dados retornados

    var producerId = 99;

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
  }
}