package org.yvesguilherme.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.commons.UserUtils;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.exception.EmailAlreadyExistsException;
import org.yvesguilherme.exception.NotFoundException;
import org.yvesguilherme.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks
  private UserService service;

  @Mock
  private UserRepository repository;

  private List<User> userList;

  @InjectMocks
  private UserUtils userUtils;

  @BeforeEach
  void init() {
    userList = userUtils.newUserList();
  }

  @Test
  @DisplayName("findAll returns a list with all Users when successful")
  void findAll_ReturnsAListWithAllUsers_WhenSuccessful() {
    BDDMockito.when(repository.findAll()).thenReturn(userList);

    var users = service.findAll();

    Assertions
            .assertThat(users)
            .isNotNull()
            .isNotEmpty()
            .hasSameElementsAs(userList);
  }

  @Test
  @DisplayName("findByIdOrThrowNotFound returns a User with given ID when successful")
  void findByIdOrThrowNotFound_ReturnsAUserWithGivenID_WhenSuccessful() {
    var expectedUser = userList.getFirst();

    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

    var userFound = service.findByIdOrThrowNotFound(expectedUser.getId());

    Assertions
            .assertThat(userFound)
            .isNotNull()
            .isEqualTo(expectedUser);
  }

  @Test
  @DisplayName("findByIdOrThrowNotFound throws NotFoundException when User is not found")
  void findByIdOrThrowNotFound_ThrowsNotFoundException_WhenUserIsNotFound() {
    var expectedUser = userList.getFirst();

    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("findByFirstName returns a list with all Users with the same first name when successful")
  void findByFirstName_ReturnsAListWithAllUsersWithTheSameFirstName_WhenSuccessful() {
    var user = userList.getFirst();
    var expectedUsersFound = singletonList(user);

    BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expectedUsersFound);

    var usersFound = service.findByFirstName(user.getFirstName());

    Assertions
            .assertThat(usersFound)
            .isNotNull()
            .isNotEmpty()
            .containsAll(expectedUsersFound);
  }

  @Test
  @DisplayName("findByFirstName returns an empty list when no User is found")
  void findByFirstName_ReturnsAnEmptyList_WhenNoUserIsFound() {
    var firstName = "not-found";

    BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.emptyList());

    var usersFound = service.findByFirstName(firstName);

    Assertions
            .assertThat(usersFound)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findByLastName returns a list with all Users with the same last name when successful")
  void findByLastName_ReturnsAListWithAllUsersWithTheSameLastName_WhenSuccessful() {
    var user = userList.getLast();
    var expectedUsersFound = singletonList(user);

    BDDMockito.when(repository.findByLastNameIgnoreCase(user.getLastName())).thenReturn(expectedUsersFound);

    var usersFound = service.findByLastName(user.getLastName());

    Assertions
            .assertThat(usersFound)
            .isNotNull()
            .isNotEmpty()
            .containsAll(expectedUsersFound);
  }

  @Test
  @DisplayName("findByLastName returns an empty list when no User is found")
  void findByLastName_ReturnsAnEmptyList_WhenNoUserIsFound() {
    var lastName = "not-found";

    BDDMockito.when(repository.findByLastNameIgnoreCase(lastName)).thenReturn(Collections.emptyList());

    var usersFound = service.findByLastName(lastName);

    Assertions
            .assertThat(usersFound)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findByEmail returns a User with the same email when successful")
  void findByEmail_ReturnsAUserWithTheSameEmail_WhenSuccessful() {
    var expectedUser = userList.getFirst();

    BDDMockito.when(repository.findByEmailIgnoreCase(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser));

    var userFound = service.findByEmailOrThrowNotFound(expectedUser.getEmail());

    Assertions
            .assertThat(userFound)
            .isNotNull()
            .isEqualTo(expectedUser);
  }

  @Test
  @DisplayName("findByEmail throws NotFoundException when User email is not found")
  void findByEmail_ThrowsNotFoundExceptionWhenUserEmailIsNotFound() {
    var emailNotFound = "not_found@notfound.com";

    BDDMockito.when(repository.findByEmailIgnoreCase(emailNotFound)).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.findByEmailOrThrowNotFound(emailNotFound))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("save returns a User when successful")
  void save_ReturnsAUser_WhenSuccessful() {
    var userToSave = userUtils.newUserToSave();

    BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
    BDDMockito.when(repository.findByEmailIgnoreCase(userToSave.getEmail())).thenReturn(Optional.empty());

    var savedUser = service.save(userToSave);

    Assertions
            .assertThat(savedUser)
            .hasNoNullFieldsOrProperties()
            .isEqualTo(userToSave);
  }

  @Test
  @DisplayName("save throws EmailAlreadyExistsException when e-mail belongs to another user")
  void save_ThrowsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
    var savedUser = userList.getLast();
    var userToSave = userUtils.newUserToSave().withEmail(savedUser.getEmail());
    var email = userToSave.getEmail();

    BDDMockito.when(repository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(savedUser));

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.save(userToSave))
            .isInstanceOf(EmailAlreadyExistsException.class);
  }

  @Test
  @DisplayName("delete removes a User when successful")
  void delete_RemovesAUser_WhenSuccessful() {
    var userToDelete = userList.getFirst();

    BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
    BDDMockito.doNothing().when(repository).delete(userToDelete);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.delete(userToDelete.getId()));
  }

  @Test
  @DisplayName("delete throws NotFoundException when User is not found")
  void delete_ThrowsNotFoundException_WhenUserIsNotFound() {
    var userToDelete = userList.getFirst();

    BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.delete(userToDelete.getId()))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("update updates a User firsName when successful")
  void update_UpdatesAUserFirstName_WhenSuccessful() {
    var userToUpdate = userList.getFirst().withFirstName("Saitama");
    var email = userToUpdate.getEmail();
    var id = userToUpdate.getId();

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
    BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(email, id)).thenReturn(Optional.empty());
    BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

    service.update(userToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(userToUpdate));
  }

  @Test
  @DisplayName("update updates a User lastName when successful")
  void update_UpdatesAUserLastName_WhenSuccessful() {
    var userToUpdate = userList.getFirst();
    userToUpdate.setLastName("Hatake");

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
    BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

    service.update(userToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(userToUpdate));
  }

  @Test
  @DisplayName("update updates a User email when successful")
  void update_UpdatesAUserEmail_WhenSuccessful() {
    var userToUpdate = userList.getFirst();
    userToUpdate.setEmail("sonGoku2025@gmail.com");

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
    BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

    service.update(userToUpdate);

    Assertions
            .assertThatNoException()
            .isThrownBy(() -> service.update(userToUpdate));
  }

  @Test
  @DisplayName("update throws NotFoundException when User is not found")
  void update_ThrowsNotFoundException_WhenUserIsNotFound() {
    var userToUpdate = userList.getFirst();

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(userToUpdate))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("update throws EmailAlreadyExistsException when e-mail belongs to another user")
  void update_ThrowsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
    var savedUser = userList.getLast();
    var userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());
    var email = userToUpdate.getEmail();
    var id = userToUpdate.getId();

    BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
    BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

    Assertions
            .assertThatException()
            .isThrownBy(() -> service.update(userToUpdate))
            .isInstanceOf(EmailAlreadyExistsException.class);
  }
}