package org.yvesguilherme.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
  @EqualsAndHashCode.Include
  private Long id;

  @JsonProperty("name")
  private String name;

  private LocalDateTime createdAt;
}



