package org.yvesguilherme.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.commons.AnimeUtils;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.repository.AnimeHardCodedRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
  @InjectMocks
  private AnimeService service;

  @Mock
  private AnimeHardCodedRepository repository;

  private List<Anime> animeList;

  @InjectMocks
  private AnimeUtils animeUtils;

  @BeforeEach
  void setUp() {
    animeList = animeUtils.newAnimeList();
  }

  @Test
  @DisplayName("findAll returns a list with all animes when argument is null")
  @Order(1)
  void findAllReturnsAllAnimesWhenArgumentIsNull() {
    BDDMockito.when(repository.findAll()).thenReturn(animeList);

    var animes = service.findAll(null);

    Assertions
            .assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSameElementsAs(animeList);
  }

  @Test
  @DisplayName("findAll returns a list with all animes when argument exists")
  @Order(2)
  void findAllReturnsAllAnimesWhenArgumentExists() {
    var anime = animeList.getFirst();
    var expectedAnimeFound = singletonList(anime);

    BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimeFound);

    var animeFound = service.findAll(anime.getName());

    Assertions
            .assertThat(animeFound)
            .containsAll(expectedAnimeFound);
  }

  @Test
  @DisplayName("findAll returns empty list when name is not found")
  @Order(3)
  void findAllReturnsEmptyListWhenNameIsNotFound() {
    var anime = "not-found";

    BDDMockito.when(repository.findByName(anime)).thenReturn(emptyList());

    var animeNotFound = service.findAll(anime);

    Assertions
            .assertThat(animeNotFound)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findById returns an Anime with given id")
  @Order(4)
  void findByIdReturnsAnAnimeWhenSuccessful() {
    var expectedAnime = animeList.getFirst();

    BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

    var animeFound = service.findByIdOrThrowNotFound(expectedAnime.getId());

    Assertions
            .assertThat(animeFound)
            .isEqualTo(expectedAnime);
  }

  @Test
  @DisplayName("findById throws ResponseStatusException when anime is not found")
  @Order(5)
  void findByIdThrowsResponseStatusExceptionWhenAnimeIsNotFound() {
    var expectedAnime = animeList.getFirst();

    BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
            .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("save creates an anime")
  @Order(6)
  void saveCreatesAnAnimeWhenSuccessful() {
    var animeToSave = animeUtils.newAnimeToSave("Hellsing");

    BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

    var savedAnime = service.save(animeToSave);

    Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete removes an anime")
  @Order(8)
  void deleteRemovesAnAnimeWhenSuccessful() {
    var animeToDelete = animeList.getFirst();

    BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
    BDDMockito.doNothing().when(repository).delete(animeToDelete);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.delete(animeToDelete.getId()));
  }

  @Test
  @DisplayName("delete throws ResponseStatusException when anime is not found")
  @Order(9)
  void deleteThrowsResponseStatusExceptionWhenAnimeIsNotFound() {
    var animeToDelete = animeList.getFirst();

    BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.delete(animeToDelete.getId()))
            .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("update updates an anime")
  @Order(10)
  void updateUpdatesAnAnimeWhenSuccessful() {
    var animeToUpdate = animeList.getFirst();
    animeToUpdate.setName("Hellsing");

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
    BDDMockito.doNothing().when(repository).update(animeToUpdate);

    service.update(animeToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(animeToUpdate));
  }

  @Test
  @DisplayName("update throws ResponseStatusException when anime is not found")
  @Order(11)
  void updateThrowsResponseStatusExceptionWhenAnimeIsNotFound() {
    var animeToUpdate = animeList.getFirst();

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(animeToUpdate))
            .isInstanceOf(ResponseStatusException.class);
  }

}