package org.yvesguilherme.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnimePostRequest {
  @NotBlank(message = "The field 'name' is required")
  private String name;
}
