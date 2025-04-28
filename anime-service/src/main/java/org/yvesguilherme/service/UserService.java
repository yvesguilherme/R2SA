package org.yvesguilherme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.exception.EmailAlreadyExistsException;
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

  @Transactional
  public User save(User user) {
    assertEmailDoesNotExist(user.getEmail());
    return repository.save(user);
  }

  @Transactional
  public void delete(Long id) {
    var user = findByIdOrThrowNotFound(id);

    repository.delete(user);
  }

  @Transactional
  public void update(User user) {
    findByIdOrThrowNotFound(user.getId());
    assertEmailDoesNotExist(user.getEmail(), user.getId());
    repository.save(user);
  }

  private void assertEmailDoesNotExist(String email) {
    repository.findByEmailIgnoreCase(email).ifPresent(this::throwEmailExistsException);
  }

  private void assertEmailDoesNotExist(String email, Long id) {
    repository.findByEmailIgnoreCaseAndIdNot(email, id).ifPresent(this::throwEmailExistsException);
  }

  private void throwEmailExistsException(User user) {
    throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
  }
}
