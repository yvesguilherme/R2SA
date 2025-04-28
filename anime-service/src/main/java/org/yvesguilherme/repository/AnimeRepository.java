package org.yvesguilherme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Anime;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
  List<Anime> findByName(String name);
  Optional<Anime> findByNameEqualsIgnoreCase(String name);
}

