package org.yvesguilherme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByFirstNameIgnoreCase(String firstName);
  List<User> findByLastNameIgnoreCase(String lastName);
  Optional<User> findByEmailIgnoreCase(String email);
  Optional<User> findByEmailIgnoreCaseAndIdNot(String email, Long id);
}
