package org.yvesguilherme.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProducerEnum {
  NOT_FOUND("Producer not found!");

  private final String message;
}
