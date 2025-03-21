package org.yvesguilherme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.AnimeUtils;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.repository.AnimeData;
import org.yvesguilherme.repository.AnimeHardCodedRepository;
import org.yvesguilherme.service.AnimeService;
import org.yvesguilherme.util.Constants;

import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = "org.yvesguilherme")
class AnimeControllerTest {
  private static final String URL = "/animes";

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

  @MockitoSpyBean
  private AnimeHardCodedRepository repository;

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

    var response = fileUtils.readResourceFile("anime/get-anime-thread-test-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/thread-test"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes returns a list with all animes when argument is null")
  void findAll_ReturnsAListOfAnimes_WhenArgumentIsNull() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/animes?name=Demon Slayer returns a list with found object when name exists")
  void findAll_ReturnsAListOfAnimes_WhenArgumentExists() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var response = fileUtils.readResourceFile("anime/get-anime-demon-slayer-name-200.json");
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

    var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
    var name = "x";

    mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /animes/1 returns an Anime with given id")
  void findById_ReturnsAnAnimeWithGivenId_WhenSuccessful() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var response = fileUtils.readResourceFile("anime/get-anime-by-id-1-200.json");
    var id = 1;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /animes/4 throws ResponseStatusException when anime is not found")
  void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
    var id = 4;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
  }

  @Test
  @DisplayName("POST /animes creates an anime")
  void save_CreatesAnAnime_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
    var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
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

  @Test
  @DisplayName("POST /anime throws BadRequestException when anime name is empty")
  void save_ThrowsBadRequestException_WhenAnimeNameIsEmpty() throws Exception {
    var request = fileUtils.readResourceFile("anime/post-request-anime-400.json");

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("The property name is invalid!"));
  }

  @Test
  @DisplayName("PUT /animes updates an anime when successful")
  void update_UpdatesAnAnime_WhenSuccessful() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("PUT /animes throws ResponseStatusException when Anime is not found")
  void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
  }

  @Test
  @DisplayName("DELETE /animes/1 removes an anime")
  void delete_RemovesAnAnime_WhenSuccessful() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animeId = animeList.getFirst().getId();

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /animes/299 throws ResponseStatusException when an anime is not found")
  void delete_ThrowsResponseStatusException_WhenAnAnimeIsNotFound() throws Exception {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animeId = 299;

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
  }
}