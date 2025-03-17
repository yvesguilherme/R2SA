package org.yvesguilherme.repository;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Anime;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class AnimeHardCodedRepository {

  private final AnimeData animeData;
  private final Connection connection;

  public List<Anime> findAll() {
    log.debug(connection);
    return animeData.getAnimeList();
  }

  public Optional<Anime> findById(Long id) {
    return animeData.getAnimeList().stream().filter(a -> a.getId().equals(id)).findFirst();
  }

  public List<Anime> findByName(String name) {
    return animeData.getAnimeList().stream().filter(a -> a.getName().equalsIgnoreCase(name)).toList();
  }

  public Anime save(Anime anime) {
    animeData.getAnimeList().add(anime);

    return anime;
  }

  public void update(Anime anime) {
    delete(anime);
    save(anime);
  }

  public void delete(Anime anime) {
    animeData.getAnimeList().removeIf(p -> p.getId().equals(anime.getId()));
  }
}
