package org.yvesguilherme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.yvesguilherme.config.ConnectionConfigurationProperties;

@SpringBootApplication
//@ComponentScan(basePackages = {"outside.yvesguilherne", "org.yvesguiherme"})
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
//@ConfigurationPropertiesScan
public class AnimeServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnimeServiceApplication.class, args);

//    var applicationContext = SpringApplication.run(AnimeServiceApplication.class, args);
//    Arrays.stream(applicationContext.getBeanDefinitionNames())
//            .forEach(System.out::println);
  }

}
