package org.yvesguilherme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "greetings")
@Slf4j
public class HelloController {
  @GetMapping()
  public String hi() {
    return "OMAE WA MOU SHIND IRU";
  }

  @PostMapping
  public Long save(@RequestBody String name) {
    log.info("save '{}'", name);
    return ThreadLocalRandom.current().nextLong(1, 1000);
  }
}
