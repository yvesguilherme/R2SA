package org.yvesguilherme.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.exception.BadRequestException;
import org.yvesguilherme.repository.AnimeHardCodedRepository;
import org.yvesguilherme.util.enums.ProducerEnum;

import java.util.List;

public class AnimeService {
  private final AnimeHardCodedRepository animeHardCodedRepository;

  public AnimeService() {
    this.animeHardCodedRepository = new AnimeHardCodedRepository();
  }

  public List<Anime> findAll(String name) {
    return name == null ? animeHardCodedRepository.findAll() : animeHardCodedRepository.findByName(name);
  }

  public Anime findByIdOrThrowNotFound(Long id) {
    return animeHardCodedRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ProducerEnum.NOT_FOUND.getMessage()));
  }

  public Anime save(Anime anime) {
    if (anime.getName().isEmpty()) {
      throw new BadRequestException("The property name is invalid!");
    }

    return animeHardCodedRepository.save(anime);
  }

  public void delete(Long id) {
    var producer = findByIdOrThrowNotFound(id);

    animeHardCodedRepository.delete(producer);
  }

  public void update(Anime animeToUpdate) {
    assertAnimeExists(animeToUpdate.getId());

    animeHardCodedRepository.update(animeToUpdate);
  }

  private void assertAnimeExists(Long id) {
    findByIdOrThrowNotFound(id);
  }
}
