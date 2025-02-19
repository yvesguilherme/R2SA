package org.yvesguilherme.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProducerPutRequest {
  private Long id;
  private String name;
}
