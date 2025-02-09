package org.yvesguilherme.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimeEnum {
  NOT_FOUND("Anime not found!");

  private final String message;
}
