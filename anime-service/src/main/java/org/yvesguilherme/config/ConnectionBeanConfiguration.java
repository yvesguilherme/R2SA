package org.yvesguilherme.config;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
  private final ConnectionConfigurationProperties configurationProperties;

//  @Value("${database.url}")
//  private String url;
//
//  @Value("${database.username}")
//  private String username;
//
//  @Value("${database.password}")
//  private String password;

  /**
   * @Bean - Utilizado para fazer injeção de dependência de um Objeto que não está no MEU controle.
   * Porém deve ser lembrado que o Spring tenta procurar na seguinte ordem:
   * 1 - Tipo do Objeto;
   * 2 - Nome do Método;
   * 3 - Nomes customizados.
   * Observe que este método é como se fosse uma Factory.
   * Também há a opção de passar um nome e utilizar o @Qualifier onde a classe está sendo instanciada,
   * para que funcione. Obs.: O arquivo lombok.config foi criado, pois ele dá erro no construtor.
   * @Primary - Diz que este método é o padrão, quando se tem mais de um @Bean,
   * porém pode ser utilizado também o nome do método.
   */
  @Bean
  @Primary
  public Connection connection() {
    return new Connection(
            configurationProperties.url(),
            configurationProperties.username(),
            configurationProperties.password()
    );
  }

  @Bean
  @Profile("mysql")
  public Connection connectionMySQL() {
    return new Connection(
            configurationProperties.url(),
            configurationProperties.username(),
            configurationProperties.password()
    );
  }

  @Bean(name = "connectionMongoDB")
  @Profile("mongo")
  public Connection connectionMongo() {
    return new Connection(
            configurationProperties.url(),
            configurationProperties.username(),
            configurationProperties.password()
    );
  }

  @Bean(name = "connectionOracle")
  @Profile("oracle")
  public Connection connectionOracle() {
    return new Connection(
            configurationProperties.url(),
            configurationProperties.username(),
            configurationProperties.password()
    );
  }

  @Bean
  @Profile("postgresql")
  public Connection connectionPostgreSQL() {
    return new Connection(
            configurationProperties.url(),
            configurationProperties.username(),
            configurationProperties.password()
    );
  }
}
