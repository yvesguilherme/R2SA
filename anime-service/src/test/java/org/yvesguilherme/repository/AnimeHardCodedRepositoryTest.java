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
import org.yvesguilherme.domain.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {

  @InjectMocks
  private AnimeHardCodedRepository repository;

  @Mock
  private AnimeData animeData;

  private List<Anime> animeList;

  @BeforeEach
  void init() {
    var fullMetalBrotherhood = Anime.builder().name("Full Metal Brotherhood").id(1L).build();
    var steinsGate = Anime.builder().name("Steins Gate").id(2L).build();
    var mashle = Anime.builder().name("Mashle").id(3L).build();

    animeList = new ArrayList<>(List.of(fullMetalBrotherhood, steinsGate, mashle));
  }

  @Test
  @DisplayName("findALl returns a list with all animes")
  void findAllReturnsAllAnimesWhenSuccessful() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animes = repository.findAll();

    Assertions
            .assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSameElementsAs(animeList);
  }

  @Test
  @DisplayName("findById returns an Anime with given id")
  void findByIdReturnsAnAnimeWhenSuccessful() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var expectedAnime = animeList.getFirst();
    var anime = repository.findById(expectedAnime.getId());

    Assertions
            .assertThat(anime)
            .isPresent()
            .contains(expectedAnime);
  }

  @Test
  @DisplayName("findByName returns a list with found object when name exists")
  void findByNameReturnsAListWithFoundObjectWhenNameExists() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var expectedAnime = animeList.getFirst();
    var animes = repository.findByName(expectedAnime.getName());

    Assertions
            .assertThat(animes)
            .contains(expectedAnime);
  }

  @Test
  @DisplayName("save creates an Anime")
  void saveCreatesAnAnimeWhenSuccessful() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animeToSave = Anime.builder().id(99L).name("Pokémon").build();
    var anime = repository.save(animeToSave);

    Assertions
            .assertThat(anime)
            .isEqualTo(animeToSave)
            .hasNoNullFieldsOrProperties();

    Optional<Anime> animeSavedOptional = repository.findById(anime.getId());
    Assertions
            .assertThat(animeSavedOptional)
            .isPresent()
            .contains(animeToSave);

  }

  @Test
  @DisplayName("update updates an Anime")
  void updateUpdatesAnAnimeWhenSuccessful() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animeToUpdate = animeData.getAnimeList().getFirst();
    animeToUpdate.setName("Dragon Ball Z");

    repository.update(animeToUpdate);

    Assertions
            .assertThat(animeList)
            .contains(animeToUpdate);

    Optional<Anime> animeUpdatedOptional = repository.findById(animeToUpdate.getId());

    Assertions
            .assertThat(animeUpdatedOptional)
            .isPresent()
            .contains(animeToUpdate);

    Assertions
            .assertThat(animeUpdatedOptional.get().getName())
            .isEqualTo(animeToUpdate.getName());
  }

  @Test
  @DisplayName("delete removes an Anime")
  void deleteRemovesAnAnimeWhenSuccessful() {
    BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

    var animeToDelete = animeData.getAnimeList().getFirst();
    repository.delete(animeToDelete);

    var animes = repository.findAll();

    Assertions
            .assertThat(animes)
            .isNotEmpty()
            .doesNotContain(animeToDelete);
  }
}