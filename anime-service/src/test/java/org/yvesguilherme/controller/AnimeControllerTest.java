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
import org.yvesguilherme.commons.AnimeUtils;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.config.ConnectionBeanConfiguration;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.mapper.AnimeMapperImpl;
import org.yvesguilherme.repository.AnimeData;
import org.yvesguilherme.repository.AnimeRepository;
import org.yvesguilherme.service.AnimeService;
import org.yvesguilherme.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@Import({
        AnimeRepository.class,
        AnimeService.class,
        AnimeMapperImpl.class,
        ConnectionBeanConfiguration.class,
        AnimeData.class,
        Constants.class,
        FileUtils.class,
        AnimeUtils.class
})
class AnimeControllerTest {
  private static final String URL = "/animes";
  private static final String THE_FIELD_NAME_IS_REQUIRED = "The field 'name' is required";
  private static final String THE_FIELD_ID_CANNOT_BE_NULL = "The field 'id' cannot be null";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private AnimeService animeService;

  @MockitoBean
  private AnimeData animeData;

  @MockitoBean
  private Constants constants;

  @MockitoBean
  private AnimeRepository repository;

  private List<Anime> animeList;

  @Autowired
  private AnimeUtils animeUtils;

  @BeforeEach
  void init() {
    animeList = animeUtils.newAnimeList();
  }

  @Test
  @DisplayName("GET /animes/thread-test returns a list of anime names")
  void listAll_ReturnsListOfAnimeNames_WhenSuccessful() throws Exception {
    var mockList = List.of("Naruto", "Pokémon");
    BDDMockito.when(constants.getListAnime()).thenReturn(mockList);

    var response = fileUtils.readResourceFile("anime/get/get-anime-thread-test-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/thread-test"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes returns a list with all animes when argument is null")
  void findAll_ReturnsAListOfAnimes_WhenArgumentIsNull() throws Exception {
    BDDMockito.when(repository.findAll()).thenReturn(animeList);

    var response = fileUtils.readResourceFile("anime/get/get-anime-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=Demon Slayer returns a list with found object when name exists")
  void findAll_ReturnsAListOfAnimes_WhenArgumentExists() throws Exception {
    var secondAnime = animeList.get(1);
    BDDMockito.when(repository.findByName(secondAnime.getName())).thenReturn(Collections.singletonList(secondAnime));

    var response = fileUtils.readResourceFile("anime/get/get-anime-demon-slayer-name-200.json");
    var name = "Demon Slayer";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=x returns an empty list when name is not found")
  void findAll_ReturnsAnEmptyList_WhenNameIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var response = fileUtils.readResourceFile("anime/get/get-anime-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /animes/1 returns an Anime with given id")
  void findById_ReturnsAnAnimeWithGivenId_WhenSuccessful() throws Exception {
    var firstAnime = animeList.getFirst();
    BDDMockito.when(repository.findById(firstAnime.getId())).thenReturn(Optional.of(firstAnime));

    var response = fileUtils.readResourceFile("anime/get/get-anime-by-id-1-200.json");
    var id = 1;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /animes/4 throws NotFoundException when anime is not found")
  void findById_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
    var expectedError = fileUtils.readResourceFile("anime/get/get-anime-by-id-404.json");

    var id = 4;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }

  @Test
  @DisplayName("POST /animes creates an anime")
  void save_CreatesAnAnime_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("anime/post/post-request-anime-200.json");
    var response = fileUtils.readResourceFile("anime/post/post-response-anime-201.json");
    var animeToSave = animeUtils.newAnimeToSave("Naruto");

    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @ParameterizedTest
  @MethodSource("postBadRequestResource")
  @DisplayName("POST /anime throws BadRequestException when fields are empty, blank or invalid")
  void save_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
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
    return Stream.of(
            Arguments.of("post/post-request-anime-blank-400.json", Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED)),
            Arguments.of("post/post-request-anime-empty-400.json", Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED))
    );
  }

  @ParameterizedTest
  @MethodSource("putBadRequestResource")
  @DisplayName("PUT /anime throws BadRequestException when fields are empty, blank or invalid")
  void put_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
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
    return Stream.of(
            Arguments.of("put/put-request-anime-blank-400.json", Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED)),
            Arguments.of("put/put-request-anime-empty-400.json", Collections.singletonList(THE_FIELD_NAME_IS_REQUIRED)),
            Arguments.of("put/put-request-anime-id-null-400.json", Collections.singletonList(THE_FIELD_ID_CANNOT_BE_NULL))
    );
  }

  @Test
  @DisplayName("PUT /animes updates an anime when successful")
  void update_UpdatesAnAnime_WhenSuccessful() throws Exception {
    var firstAnime = animeList.getFirst();
    BDDMockito.when(repository.findById(firstAnime.getId())).thenReturn(Optional.of(firstAnime));

    var request = fileUtils.readResourceFile("anime/put/put-request-anime-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("PUT /animes throws NotFoundException when Anime is not found")
  void update_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var request = fileUtils.readResourceFile("anime/put/put-request-anime-404.json");
    var expectedError = fileUtils.readResourceFile("anime/put/put-response-anime-404.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }

  @Test
  @DisplayName("DELETE /animes/1 removes an anime")
  void delete_RemovesAnAnime_WhenSuccessful() throws Exception {
    var firstAnime = animeList.getFirst();
    BDDMockito.when(repository.findById(firstAnime.getId())).thenReturn(Optional.of(firstAnime));

    var animeId = firstAnime.getId();

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /animes/299 throws NotFoundException when an anime is not found")
  void delete_ThrowsNotFoundException_WhenAnAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
    var expectedError = fileUtils.readResourceFile("anime/delete/delete-response-anime-by-id-404.json");

    var animeId = 299;

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }
}