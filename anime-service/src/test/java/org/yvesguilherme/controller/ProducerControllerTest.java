package org.yvesguilherme.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.commons.ProducerUtils;
import org.yvesguilherme.config.ConnectionBeanConfiguration;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.mapper.ProducerMapperImpl;
import org.yvesguilherme.repository.ProducerData;
import org.yvesguilherme.repository.ProducerRepository;
import org.yvesguilherme.service.ProducerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class)
@Import({
        ProducerRepository.class,
        ConnectionBeanConfiguration.class,
        ProducerData.class,
        ProducerMapperImpl.class,
        ProducerService.class,
        FileUtils.class,
        ProducerUtils.class
})
//@ComponentScan(basePackages = "org.yvesguilherme")
//@ActiveProfiles("test")
class ProducerControllerTest {
  private static final String URL = "/producers";
  private static final String THE_FIELD_NAME_IS_REQUIRED = "The field 'name' is required";
  private static final String THE_FIELD_ID_CANNOT_BE_NULL = "The field 'id' cannot be null";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProducerData producerData;

  @MockitoBean
  private ProducerRepository repository;

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
    BDDMockito.when(repository.findAll()).thenReturn(producerList);  // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get/get-producer-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers?name=Ufotable returns a list with found object when name exists")
  void findAll_ReturnsAListWithFoundObject_WhenNameExists() throws Exception {
    var firstProducer = producerList.getFirst();
    BDDMockito.when(repository.findByNameIgnoreCase(firstProducer.getName())).thenReturn(Collections.singletonList(firstProducer)); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get/get-producer-ufotable-name-200.json");
    var name = "Ufotable";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers?name=x returns an empty list when name is not found")
  void findAll_ReturnsAnEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList()); // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var response = fileUtils.readResourceFile("producer/get/get-producer-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers/1 returns a Producer with given id")
  void findById_ReturnsAProducerWithGivenId_WhenSuccessful() throws Exception {
    var firstProducer = producerList.getFirst();
    BDDMockito.when(repository.findById(firstProducer.getId())).thenReturn(Optional.of(firstProducer)); // Não temos domínio sobre os dados retornados

    var response = fileUtils.readResourceFile("producer/get/get-producer-by-id-1-200.json");
    var id = 1;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /producers/4 throws ResponseStatusException 404 when Producer is not found")
  void findById_ThrowsResponseStatusException404_WhenProducerIsNotFound() throws Exception {
    var id = 4L;

    BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty()); // Não temos domínio sobre os dados retornados

    var expectedError = fileUtils.readResourceFile("producer/get/get-producer-by-id-404.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }

  @Test
  @DisplayName("POST /producers creates a Producer when successful")
  void save_CreatesAProducer_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("producer/post/post-request-producer-200.json");
    var response = fileUtils.readResourceFile("producer/post/post-response-producer-201.json");
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

  @ParameterizedTest
  @MethodSource("postBadRequestResource")
  @DisplayName("POST /producers throws BadRequestException when fields are empty, blank or invalid")
  void save_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("x-api-key", "1234")
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions
            .assertThat(resolvedException.getMessage())
            .contains(errors);
  }

  private static Stream<Arguments> postBadRequestResource() {
    var fieldNameError = Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED);

    return Stream.of(
            Arguments.of("post/post-request-producer-blank-400.json", fieldNameError),
            Arguments.of("post/post-request-producer-empty-400.json", fieldNameError)
    );
  }

  @ParameterizedTest
  @MethodSource("putBadRequestResource")
  @DisplayName("POST /producers throws BadRequestException when fields are empty, blank or invalid")
  void update_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("x-api-key", "1234")
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions
            .assertThat(resolvedException.getMessage())
            .contains(errors);
  }

  private static Stream<Arguments> putBadRequestResource() {
    var fieldIdError = Collections.singletonList(THE_FIELD_ID_CANNOT_BE_NULL);
    var fieldNameError = Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED);

    return Stream.of(
            Arguments.of("put/put-request-producer-blank-400.json", fieldNameError),
            Arguments.of("put/put-request-producer-empty-400.json", fieldNameError),
            Arguments.of("put/put-request-producer-id-null-400.json", fieldIdError)
    );
  }

  @Test
  @DisplayName("PUT /producers updates a Producer when successful")
  void update_UpdatesAProducer_WhenSuccessful() throws Exception {
    var firstProducer = producerList.getFirst();
    BDDMockito.when(repository.findById(firstProducer.getId())).thenReturn(Optional.of(firstProducer));  // Não temos domínio sobre os dados retornados, devemos mocá-los.

    var request = fileUtils.readResourceFile("producer/put/put-request-producer-200.json");

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
    BDDMockito.when(repository.findById(99L)).thenReturn(Optional.empty()); // Não temos domínio sobre os dados retornados

    var request = fileUtils.readResourceFile("producer/put/put-request-producer-404.json");
    var expectedError = fileUtils.readResourceFile("producer/put/put-response-producer-404.json");

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));

  }

  @Test
  @DisplayName("DELETE /producers/1 delete removes a producer")
  void delete_RemovesProducer_WhenSuccessful() throws Exception {
    var firstProducer = producerList.getFirst();
    BDDMockito.when(repository.findById(firstProducer.getId())).thenReturn(Optional.of(firstProducer));  // Não temos domínio sobre os dados retornados, devemos mocá-los.


    var producerId = firstProducer.getId();

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /producers/99 throws ResponseStatusException when producer is not found")
  void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception {
    var producerId = 99L;

    BDDMockito.when(repository.findById(producerId)).thenReturn(Optional.empty()); // Não temos domínio sobre os dados retornados

    var expectedError = fileUtils.readResourceFile("producer/delete/delete-response-producer-by-id-404.json");


    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", producerId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }
}