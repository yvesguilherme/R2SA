package org.yvesguilherme.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
  @EqualsAndHashCode.Include
  private Long id;

  @NonNull
  private String firstName;

  @NonNull
  private String lastName;

  @NonNull
  private String email;
}
