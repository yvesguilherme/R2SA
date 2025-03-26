package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.repository.UserHardCodedRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  public static final String USER_NOT_FOUND = "User not found";

  private final UserHardCodedRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findByIdOrThrowNotFound(Long id) {
    return repository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
  }

  public List<User> findByFirstName(String firstName) {
    return repository.findByFirstName(firstName);
  }

  public List<User> findByLastName(String lastName) {
    return repository.findByLastName(lastName);
  }

  public User findByEmailOrThrowNotFound(String email) {
    return repository
            .findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
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

    repository.update(user);
  }
}
