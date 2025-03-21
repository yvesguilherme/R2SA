package org.yvesguilherme.validator;

import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.User;

@Component
public class UserValidator {
  public void validateUser(User user) {
    if (user.getFirstName().isEmpty()) {
      throw new IllegalArgumentException("The property firstName is invalid!");
    }

    if (user.getLastName().isEmpty()) {
      throw new IllegalArgumentException("The property lastName is invalid!");
    }

    if (user.getEmail().isEmpty()) {
      throw new IllegalArgumentException("The property email is invalid!");
    }
  }
}
