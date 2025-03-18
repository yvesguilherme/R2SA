### VM params
 - -Xmx500m -> Limita a JVM a 500mb
 - -XshowSettings:vm -> Mostra as settings da JVM

### Threads virtuais
- Você vê a diferença quando está trabalhando com entrada e saída de dados, conexão com banco, outro serviço, lendo arquivo, etc.
- Quando está utilizando a CPU não dá para ver tanto a diferença.

### Platform Threads
- Tomcat, Jetty

### Jackson
- Object -> JSON   = GET
- JSON   -> Object = SET

### Idempotente - RFC 7231
Um método HTTP é idempotente se uma requisição idêntica pode ser feita uma ou mais vezes em sequência com o mesmo efeito enquanto deixa o servidor no mesmo estado. Em outras palavras, um método idempotente não deveria possuir nenhum efeito colateral (exceto para manter estatísticas).

- **GET, HEAD, PUT, e DELETE** são métodos idempotente
- **POST** não é idempotente

### Desacoplar o código
- O código de domínio nunca deve ser exposto para os clientes e/ou outros sistemas

### Injeção de dependência
- Quando o Spring cria o objeto para nós.

### Spring bean
- Objeto gerenciado pelo spring, o spring precisa reconhecer a classe como um Bean, e isso se dá através da anotação ```@Component```;
- Por qual motivo não utilizar o ```@Autowired``` e sim um construtor com argumentos?
  - Imutabilidade ```final``` na declaração do atributo;
    - Se tiver usando Lombok: ```@RequiredArgsConstructor```, cria um construtor com todos os atributos final;
  - Facilidade com os testes unitários;
  - Princípio da responsabilidade única.

### Tests
#### Cobertura de testes é importante, mas talvez seja mais importante ter uma quantidade de cenários diferentes como: ```sunny day``` verificar se a lógica está funcionando e ```rainy day``` o que aconteceria se as coisas não estiverem conforme você espera.
#### Exemplo: Artigo [Use case testing](https://www.softwaretestinghelp.com/use-case-testing/)
* ```@ExtendWith(SpringExtension.class)```: Use esta anotação com a classe **SpringExtension** quando você quer usar as funcionalidades do Spring.
* ```@ExtendWith(MockitoExtension.class)```: Use esta anotação com a classe **MockitoExtension** quando você quer mocar os dados que estão fora da classe do teste.
* ```@InjectMocks```: Cria o objeto anotado, pois dentro da classe de testes não temos o Spring.
* ```@DisplayName("findAll returns a list with all producers")```: Serve para facilitar a descrição do teste.
* ```@Mock```: Serve para dar o comportamento de mock para o objeto, pois se não der comportamento será ```null```.
* ```@Order(n)```: Serve para deixar explícito em qual ordem será realizado aquele teste, porém é necessário também adicionar a anotação ```@TestMethodOrder(MethodOrderer.OrderAnnotation.class)``` na classe.
* Testes de controller:
  * ```@SpringBootTest```: Starta um servidor e faz algumas configurações para rodar os testes. Caso queira fazer um teste de integração, esta anotação é necessária ```@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)```.
  * ```@AutoConfigureMockMvc```: Permite a configuração de alguns clientes para utilizar para fazer chamadas.
  * ```@MockMvc(controllers = XPTO.class)```: Permite fazer chamadas para o controller como se fosse um teste de integração, porém ele é um **_sliced test_**, carrega parcialmente alguns beans, permitindo que os testes sejam realizados mais rapidamente.
    * Não carrega ```@Component @Repository @Service```
  * ```@Import({})```: Importa as classes de configuração necessárias para rodar o teste. Ex: ```Service.class, Repository.class, Mapper.class, etc.```.
    * Também é possível não utilizar esta anotação e utilizar a ```@ComponentScan(basePackages = "org.yvesguilherme"```, assim fica mais limpa a classe e é melhor num cenário que tenha muitos beans.
  * ```@SpyBean/@MockitoSpyBean```: Serve para fazer um mock parcial, ou seja, ele vai chamar o método real, porém se tiver algum método que você queira mockar o comportamento ex: ```save()```, você pode fazer isso.

### Profiles
- Utilizado para o DTAP(Development, testing, acceptance and production)
- Para passar o profile é necessário utilizar a opção:
  - ```Build and run -> Modify options -> Add VM options -> -Dspring.profiles.active=stg``` no momento de rodar a aplicação
  - ```Build and run -> Modify options -> Program arguments -> --spring.profiles.active=stg``` no momento de rodar a aplicação
  - Se estiver o ```Intellij Ultimate``` é somente setar no active profile
  - No ```application.yaml``` é possível setar o profile ativo: ```spring.profiles.active:dev,prd```.
- Ele irá pegar tudo do application.yaml (padrão) e colocará no profile ativo application-stg.yaml ou application-prd.yaml.
- Uma maneira é utilizar a anotação ```@ActiveProfiles("PERFIL")``` em cada classe de teste.
- É sempre bom ter um profile para testes, pois você garante que o ambiente de testes não esteja em produção.
  - No gradle basta adicionar:
    - ```shell
        sourceSets {
          test {
            java {
              srcDirs = ['src/test/java']
            }
            resources {
              srcDirs = ['src/test/resources']
            }
          }
        }
      ```
    - ```shell
        test { 
          systemProperty "spring.profiles.active", "test" 
        }
      ``` 
  - No maven basta procurar:
    - ```<maven-surefire-plugin>``` e adicionar:
      - ```<configuration>```
        - ```<argLine>-Dspring.profiles.active=test</argLine>```
      - ```</configuration>```