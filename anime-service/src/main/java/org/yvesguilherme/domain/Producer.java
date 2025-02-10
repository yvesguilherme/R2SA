package org.yvesguilherme.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.yvesguilherme.util.Constants;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Producer {
  private Long id;

  @NonNull
  @JsonProperty("name")
  private String name;

  @Getter
  private static List<Producer> producers = new ArrayList<>();

  static {
    for (int x = 0; x < Constants.PRODUCERS.size(); x++) {
      producers.add(
              new Producer(x + 1L, Constants.PRODUCERS.get(x))
      );
    }
  }
}



