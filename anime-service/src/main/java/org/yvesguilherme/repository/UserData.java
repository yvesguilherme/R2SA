package org.yvesguilherme.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserData {
  private final List<User> userList = new ArrayList<>();

  {
    var user1 = User.builder().id(1L).firstName("Goku").lastName("Saiyan").email("goku@saiyan.com").build();
    var user2 = User.builder().id(2L).firstName("Vegeta").lastName("Saiyan").email("vegeta@saiyan.com").build();
    var user3 = User.builder().id(3L).firstName("Gohan").lastName("Saiyan").email("gohan@saiyan.com").build();

    userList.addAll(List.of(user1, user2, user3));
  }
}
