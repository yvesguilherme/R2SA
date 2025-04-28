package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.AnimeAlreadyExistsException;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.AnimeRepository;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeRepository animeRepository;

  public List<Anime> findAll(String name) {
    return name == null ? animeRepository.findAll() : animeRepository.findByName(name);
  }

  public Anime findByIdOrThrowNotFound(Long id) {
    return animeRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(AnimeEnum.NOT_FOUND.getMessage()));
  }

  @Transactional
  public Anime save(Anime anime) {
    assertAnimeNameDoesNotExist(anime.getName());

    return animeRepository.save(anime);
  }

  @Transactional
  public void delete(Long id) {
    var anime = findByIdOrThrowNotFound(id);

    animeRepository.delete(anime);
  }

  @Transactional
  public void update(Anime animeToUpdate) {
    assertAnimeExists(animeToUpdate.getId());
    assertAnimeNameDoesNotExist(animeToUpdate.getName());
    animeRepository.save(animeToUpdate);
  }

  private void assertAnimeExists(Long id) {
    findByIdOrThrowNotFound(id);
  }

  private void assertAnimeNameDoesNotExist(String name) {
    animeRepository.findByNameEqualsIgnoreCase(name).ifPresent(this::throwAnimeAlreadyExistsException);
  }

  private void throwAnimeAlreadyExistsException(Anime anime) {
    throw new AnimeAlreadyExistsException("Anime with name %s already exists".formatted(anime.getName()));
  }
}
