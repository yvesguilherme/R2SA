package org.yvesguilherme.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Producer {
  private Long id;

  @NonNull
  @JsonProperty("name")
  private String name;

  private LocalDateTime createdAt;
}



