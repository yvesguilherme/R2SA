package org.yvesguilherme.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.commons.UserUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

  private UserValidator userValidator;

  @InjectMocks
  private UserUtils userUtils;

  @BeforeEach
  void setUp() {
    userValidator = new UserValidator();
  }

  @Test
  @DisplayName("Should not throw exception when user is valid")
  void shouldNotThrowException_WhenUserIsValid() {
    var user = userUtils.newUserToSave();

    assertDoesNotThrow(() -> userValidator.validateUser(user));
  }

  @Test
  @DisplayName("Should throw exception when User has invalid first name")
  void shouldThrowException_WhenUserHasInvalidFirstName() {
    var user = userUtils.newUserToSave();
    user.setFirstName("");

    assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user));

    var errorMessage = assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user)).getMessage();

    Assertions.assertEquals("The property firstName is invalid!", errorMessage);
  }


  @Test
  @DisplayName("Should throw exception when User has invalid last name")
  void shouldThrowException_WhenUserHasInvalidLastName() {
    var user = userUtils.newUserToSave();
    user.setLastName("");

    assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user));

    var errorMessage = assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user)).getMessage();

    Assertions.assertEquals("The property lastName is invalid!", errorMessage);
  }


  @Test
  @DisplayName("Should throw exception when User has invalid email")
  void shouldThrowException_WhenUserHasInvalidEmail() {
    var user = userUtils.newUserToSave();
    user.setEmail("");

    assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user));

    var errorMessage = assertThrows(IllegalArgumentException.class, () -> userValidator.validateUser(user)).getMessage();

    Assertions.assertEquals("The property email is invalid!", errorMessage);
  }
}