package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  public static final String USER_NOT_FOUND = "User not found";

  private final UserRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findByIdOrThrowNotFound(Long id) {
    return repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
  }

  public List<User> findByFirstName(String firstName) {
    return repository.findByFirstNameIgnoreCase(firstName);
  }

  public List<User> findByLastName(String lastName) {
    return repository.findByLastNameIgnoreCase(lastName);
  }

  public User findByEmailOrThrowNotFound(String email) {
    return repository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
  }

  public User save(User user) {
    return repository.save(user);
  }

  public void delete(Long id) {
    var user = findByIdOrThrowNotFound(id);

    repository.delete(user);
  }

  public void update(User user) {
    findByIdOrThrowNotFound(user.getId());

    repository.save(user);
  }
}
