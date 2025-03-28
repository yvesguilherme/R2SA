package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.repository.AnimeHardCodedRepository;
import org.yvesguilherme.util.enums.AnimeEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeHardCodedRepository animeHardCodedRepository;

  public List<Anime> findAll(String name) {
    return name == null ? animeHardCodedRepository.findAll() : animeHardCodedRepository.findByName(name);
  }

  public Anime findByIdOrThrowNotFound(Long id) {
    return animeHardCodedRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AnimeEnum.NOT_FOUND.getMessage()));
  }

  public Anime save(Anime anime) {
    return animeHardCodedRepository.save(anime);
  }

  public void delete(Long id) {
    var anime = findByIdOrThrowNotFound(id);

    animeHardCodedRepository.delete(anime);
  }

  public void update(Anime animeToUpdate) {
    assertAnimeExists(animeToUpdate.getId());

    animeHardCodedRepository.update(animeToUpdate);
  }

  private void assertAnimeExists(Long id) {
    findByIdOrThrowNotFound(id);
  }
}
