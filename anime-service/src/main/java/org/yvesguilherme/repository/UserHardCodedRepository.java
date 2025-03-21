package org.yvesguilherme.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class UserHardCodedRepository {
  private final UserData userData;

  public List<User> findAll() {
    return userData.getUserList();
  }

  public Optional<User> findById(Long id) {
    return userData.getUserList()
            .stream()
            .filter(a -> a.getId().equals(id))
            .findFirst();
  }

  public List<User> findByFirstName(String firstName) {
    return userData.getUserList()
            .stream()
            .filter(user -> user.getFirstName().equalsIgnoreCase(firstName))
            .toList();
  }

  public List<User> findByLastName(String lastName) {
    return userData.getUserList()
            .stream()
            .filter(user -> user.getLastName().equalsIgnoreCase(lastName))
            .toList();
  }

  public Optional<User> findByEmail(String email) {
    return userData.getUserList()
            .stream()
            .filter(user -> user.getEmail().equalsIgnoreCase(email))
            .findFirst();
  }

  public User save(User user) {
    userData.getUserList().add(user);

    return user;
  }

  public void update(User user) {
    delete(user);
    save(user);
  }

  public void delete(User user) {
    userData.getUserList().removeIf(u -> u.getId().equals(user.getId()));
  }
}
