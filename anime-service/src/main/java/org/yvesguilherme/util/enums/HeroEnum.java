package org.yvesguilherme.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeroEnum {
  NOT_FOUND("Hero not found!");

  private final String message;
}
