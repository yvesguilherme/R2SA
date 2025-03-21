package org.yvesguilherme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserGetResponse {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
}
