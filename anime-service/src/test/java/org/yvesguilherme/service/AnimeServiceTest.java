package org.yvesguilherme.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.commons.AnimeUtils;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.AnimeAlreadyExistsException;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.AnimeRepository;

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
  private AnimeRepository repository;

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
  @DisplayName("findById throws NotFoundException when anime is not found")
  @Order(5)
  void findByIdThrowsNotFoundExceptionWhenAnimeIsNotFound() {
    var expectedAnime = animeList.getFirst();

    BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
            .isInstanceOf(NotFoundException.class);
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
  @DisplayName("save throws AnimeAlreadyExistsException when anime exists")
  @Order(7)
  void save_ThrowsAnimeAlreadyExistsException_WhenAnimeExists() {
    var savedAnime = animeList.getLast();
    var animeToSave = "My Hero Academia";

    BDDMockito.when(repository.findByNameEqualsIgnoreCase(animeToSave)).thenReturn(Optional.of(savedAnime));


    Assertions
            .assertThatException()
            .isThrownBy(() -> service.save(savedAnime))
            .isInstanceOf(AnimeAlreadyExistsException.class);
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
  @DisplayName("delete throws NotFoundException when anime is not found")
  @Order(9)
  void deleteThrowsNotFoundExceptionWhenAnimeIsNotFound() {
    var animeToDelete = animeList.getFirst();

    BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.delete(animeToDelete.getId()))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("update updates an anime")
  @Order(10)
  void updateUpdatesAnAnimeWhenSuccessful() {
    var animeToUpdate = animeList.getFirst();
    animeToUpdate.setName("Hellsing");

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
    BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);

    service.update(animeToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(animeToUpdate));
  }

  @Test
  @DisplayName("update throws NotFoundException when anime is not found")
  @Order(11)
  void updateThrowsNotFoundExceptionWhenAnimeIsNotFound() {
    var animeToUpdate = animeList.getFirst();

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(animeToUpdate))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("update throws AnimeAlreadyExistsException when anime exists")
  @Order(12)
  void update_ThrowsAnimeAlreadyExistsException_WhenAnimeExists() {
    var animeSaved = animeList.getFirst();
    var animeToUpdate = animeList.getLast().withName(animeSaved.getName());

    BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
    BDDMockito.when(repository.findByNameEqualsIgnoreCase(animeToUpdate.getName())).thenReturn(Optional.of(animeSaved));

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(animeToUpdate))
            .isInstanceOf(AnimeAlreadyExistsException.class);
  }

}