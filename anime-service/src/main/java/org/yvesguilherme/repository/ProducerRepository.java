package org.yvesguilherme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.Producer;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
  List<Producer> findByNameIgnoreCase(String name);
  Optional<Producer> findByNameEqualsIgnoreCase(String name);
}
