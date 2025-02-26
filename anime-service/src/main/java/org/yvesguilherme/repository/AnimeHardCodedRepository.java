package org.yvesguilherme.repository;

import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
  private static final List<Anime> ANIMES = new ArrayList<>();

  static {
    var ninja = Anime.builder().name("Ninja Kamui").id(1L).build();
    var kaijuu = Anime.builder().name("Kaijuu-8gou").id(2L).build();
    var haikyuu = Anime.builder().name("Haikyuu!! Second Season").id(3L).build();
    var hunter = Anime.builder().name("Hunter x Hunter (2011)").id(4L).build();
    var jujutsu = Anime.builder().name("Jujutsu Kaisen").id(5L).build();
    var gintama = Anime.builder().name("Gintama': Enchousen").id(6L).build();
    var demon = Anime.builder().name("Demon Slayer: Kimetsu no Yaiba").id(7L).build();

    ANIMES.addAll(List.of(ninja, kaijuu, haikyuu, hunter, jujutsu, gintama, demon));
  }

  public List<Anime> findAll() {
    return ANIMES;
  }

  public Optional<Anime> findById(Long id) {
    return ANIMES.stream().filter(a -> a.getId().equals(id)).findFirst();
  }

  public List<Anime> findByName(String name) {
    return ANIMES.stream().filter(a -> a.getName().toLowerCase().contains(name.toLowerCase())).toList();
  }

  public Anime save(Anime anime) {
    ANIMES.add(anime);

    return anime;
  }

  public void update(Anime anime) {
    delete(anime);
    save(anime);
  }

  public void delete(Anime anime) {
    ANIMES.removeIf(p -> p.getId().equals(anime.getId()));
  }
}
