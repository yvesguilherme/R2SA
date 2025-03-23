package org.yvesguilherme.repository;

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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserHardCodedRepositoryTest {

  @InjectMocks
  private UserHardCodedRepository repository;

  @Mock
  private UserData userData;

  private List<User> userList;

  @InjectMocks
  private UserUtils userUtils;

  @BeforeEach
  void init() {
    userList = userUtils.newUserList();
  }

  @Test
  @DisplayName("findAll returns a list with all Users")
  void findAll_ReturnsAListWithAllUsers_WhenSuccessful() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var users = repository.findAll();

    Assertions
            .assertThat(users)
            .isNotNull()
            .isNotEmpty()
            .hasSameElementsAs(userList);
  }

  @Test
  @DisplayName("findById returns a User with given id")
  void findById_ReturnsAUserWithGivenId_WhenSuccessful() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var expectedUser = userData.getUserList().getFirst();
    var user = repository.findById(expectedUser.getId());

    Assertions
            .assertThat(user)
            .isPresent()
            .contains(expectedUser);
  }

  @Test
  @DisplayName("findByFirstName returns an empty list when firstName is null")
  void findByFirstName_ReturnsAnEmptyList_WhenFirstNameIsNull() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var emptyList = repository.findByFirstName(null);

    Assertions
            .assertThat(emptyList)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findByFirstName returns a list with found object when firstName exists")
  void findByFirstName_ReturnsAListWithFoundObject_WhenFirstNameExists() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var expectedUser = userData.getUserList().getFirst();
    var users = repository.findByFirstName(expectedUser.getFirstName());

    Assertions
            .assertThat(users)
            .isNotNull()
            .isNotEmpty()
            .contains(expectedUser);
  }

  @Test
  @DisplayName("findByLastName returns an empty list when lastName is null")
  void findByLastName_ReturnsAnEmptyList_WhenLastNameIsNull() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var emptyList = repository.findByLastName(null);

    Assertions
            .assertThat(emptyList)
            .isNotNull()
            .isEmpty();
  }

  @Test
  @DisplayName("findByLastName returns a list with found object when lastName exists")
  void findByLastName_ReturnsAListWithFoundObject_WhenLastNameExists() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var expectedUser = userData.getUserList().getFirst();
    var users = repository.findByLastName(expectedUser.getLastName());

    Assertions
            .assertThat(users)
            .isNotNull()
            .isNotEmpty()
            .contains(expectedUser);
  }

  @Test
  @DisplayName("findByEmail returns empty when email is null")
  void findByEmail_ReturnsAnEmptyList_WhenEmailIsNull() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var nullPointer = repository.findByEmail(null);

    Assertions
            .assertThat(nullPointer)
            .isEmpty();
  }

  @Test
  @DisplayName("findByEmail returns empty when email is not found")
  void findByEmail_ReturnsAnEmptyList_WhenEmailIsNull2() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);
    var userEmail = "not-found@not-found.com";

    var nullPointer = repository.findByEmail(userEmail);

    Assertions
            .assertThat(nullPointer)
            .isEmpty();
  }

  @Test
  @DisplayName("findByEmail returns a User when email exists")
  void findByEmail_ReturnsAUser_WhenEmailExists() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var expectedUser = userData.getUserList().getLast();
    var users = repository.findByEmail(expectedUser.getEmail());

    Assertions
            .assertThat(users)
            .isPresent()
            .contains(expectedUser);
  }

  @Test
  @DisplayName("save returns a User when successful")
  void save_ReturnsAUser_WhenSuccessful() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var userToSave = userUtils.newUserToSave();
    var user = repository.save(userToSave);

    Assertions
            .assertThat(user)
            .isNotNull()
            .isEqualTo(userToSave)
            .hasNoNullFieldsOrProperties();

    var userSavedOptional = repository.findById(userToSave.getId());

    Assertions
            .assertThat(userSavedOptional)
            .isPresent()
            .contains(userToSave);
  }

  @Test
  @DisplayName("delete removes a User when successful")
  void delete_RemovesAUser_WhenSuccessful() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var userToDelete = userData.getUserList().getFirst();
    repository.delete(userToDelete);

    var userDeletedOptional = repository.findAll();

    Assertions
            .assertThat(userDeletedOptional)
            .isNotEmpty()
            .doesNotContain(userToDelete);
  }

  @Test
  @DisplayName("update updates a User when successful")
  void update_UpdatesAUser_WhenSuccessful() {
    BDDMockito.when(userData.getUserList()).thenReturn(userList);

    var userToUpdate = userData.getUserList().getFirst();
    userToUpdate.setFirstName("Naruto");
    userToUpdate.setLastName("Uzumaki");
    userToUpdate.setEmail("naruto@uzumaki.node");

    repository.update(userToUpdate);

    Assertions
            .assertThat(this.userList)
            .contains(userToUpdate);

    var userUpdatedOptional = repository.findById(userToUpdate.getId());

    Assertions
            .assertThat(userUpdatedOptional)
            .isPresent();

    Assertions
            .assertThat(userUpdatedOptional.get().getFirstName())
            .isEqualTo(userToUpdate.getFirstName());

    Assertions
            .assertThat(userUpdatedOptional.get().getLastName())
            .isEqualTo(userToUpdate.getLastName());

    Assertions
            .assertThat(userUpdatedOptional.get().getEmail())
            .isEqualTo(userToUpdate.getEmail());
  }
}