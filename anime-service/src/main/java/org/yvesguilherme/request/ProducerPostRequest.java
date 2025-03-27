package org.yvesguilherme.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProducerPostRequest {
  @NotBlank(message = "The field 'name' is required")
  private String name;
}
