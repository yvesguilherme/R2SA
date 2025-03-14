package org.yvesguilherme.commons;

import org.springframework.stereotype.Component;
import org.yvesguilherme.domain.Producer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {
  public List<Producer> newProducerList() {
    var localDateTime = createMockLocalDateTime();

    var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
    var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
    var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();

    return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
  }

  public Producer newProducerToSave(String name) {
    var nameToSave = name == null ? "Mappa" : name;
    return Producer.builder().id(99L).name(nameToSave).createdAt(LocalDateTime.now()).build();
  }

  private static LocalDateTime createMockLocalDateTime() {
    var dateTime = "2025-03-11T11:04:13.658154042";
    var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }
}
