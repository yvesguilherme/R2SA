package org.yvesguilherme.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class FileUtils {
  @Autowired
  private ResourceLoader resourceLoader;

  public String readResourceFile(String fileName) throws IOException {
    var file = resourceLoader.getResource("classpath:%s".formatted(fileName));

    return new String(Files.readAllBytes(file.getFile().toPath()));
  }
}
