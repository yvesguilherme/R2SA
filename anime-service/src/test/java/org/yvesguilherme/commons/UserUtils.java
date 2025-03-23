package org.yvesguilherme.commons;

import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
  public List<User> newUserList() {
    var user1 = User.builder().id(1L).firstName("Goku").lastName("Saiyan").email("goku@saiyan.com").build();
    var user2 = User.builder().id(2L).firstName("Vegeta").lastName("Saiyan").email("vegeta@saiyan.com").build();
    var user3 = User.builder().id(3L).firstName("Gohan").lastName("Saiyan").email("gohan@saiyan.com").build();

    return new ArrayList<>(List.of(user1, user2, user3));
  }

  public User newUserToSave() {
    return User.builder()
            .id(999L)
            .firstName("Madara")
            .lastName("Uchiha")
            .email("uchiha@gods.com")
            .build();
  }
}
