package org.yvesguilherme.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.util.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(HeroController.class)
@ComponentScan(basePackages = "org.yvesguilherme")
class HeroControllerTest {
  private static final String URL = "/heroes";
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FileUtils fileUtils;

  @MockitoBean
  private Constants constants;

  private final List<String> listHeroes = new ArrayList<>();

  @BeforeEach
  void setUp() {
    listHeroes.addAll(List.of("All Might", "Endeavor", "Hawks", "Best Jeanist", "Edgeshot", "Mirko"));
  }

  @Test
  @DisplayName("GET /heroes returns a list of heroes when successful")
  void findAll_ReturnsListOfHeroes_WhenSuccessful() throws Exception {
    BDDMockito.when(constants.getListHeroes()).thenReturn(listHeroes);

    var response = fileUtils.readResourceFile("hero/get-hero-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /heroes/filter returns a list of heroes when successful")
  void listAllHeroesParam_ReturnsListOfHeroes_WhenSuccessful() throws Exception {
    BDDMockito.when(constants.getListHeroes()).thenReturn(listHeroes);

    var response = fileUtils.readResourceFile("hero/get-hero-filter-200.json");
    var name = "All Might";

    mockMvc.perform(MockMvcRequestBuilders.get(URL +"/filter").param("name", name))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /heroes/filterList returns a list of heroes when successful")
  void listAllHeroesParamList_ReturnsListOfHeroes_WhenSuccessful() throws Exception {
    BDDMockito.when(constants.getListHeroes()).thenReturn(listHeroes);

    var response = fileUtils.readResourceFile("hero/get-hero-filter-list-200.json");
    var names = List.of("All Might", "Endeavor").toArray(String[]::new);

    mockMvc.perform(MockMvcRequestBuilders.get(URL +"/filterList").param("names", names))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET /heroes/{name} returns a hero when successful")
  void findByName_ReturnsHero_WhenSuccessful() throws Exception {
    BDDMockito.when(constants.getListHeroes()).thenReturn(listHeroes);

    var response = fileUtils.readResourceFile("hero/get-hero-name-200.txt");
    var name = "Mirko";

    mockMvc.perform(MockMvcRequestBuilders.get(URL +"/%s".formatted(name)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(MockMvcResultMatchers.content().string(response));
  }

  @Test
  @DisplayName("GET /heroes/{name} returns a message when hero is not found")
  void findByName_ReturnsMessage_WhenHeroIsNotFound() throws Exception {
    BDDMockito.when(constants.getListHeroes()).thenReturn(listHeroes);

    var response = "Hero not found!";
    var name = "Yves";

    mockMvc.perform(MockMvcRequestBuilders.get(URL +"/%s".formatted(name)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(MockMvcResultMatchers.content().string(response));
  }
}