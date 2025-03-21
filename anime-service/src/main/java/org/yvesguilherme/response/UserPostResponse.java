package org.yvesguilherme.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPostResponse {
  private Long id;
  private String firstName;
}
