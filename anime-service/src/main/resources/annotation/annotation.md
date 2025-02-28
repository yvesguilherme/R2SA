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
* ```@ExtendWith(SpringExtension.class)```: Use esta anotação com a classe **SpringExtension** quando você quer usar as funcionalidades do Spring.
* ```@ExtendWith(MockitoExtension.class)```: Use esta anotação com a classe **MockitoExtension** quando você quer mocar os dados que estão fora da classe do teste.
* ```@InjectMocks```: Cria o objeto anotado, pois dentro da classe de testes não temos o Spring.
* ```@DisplayName("findAll returns a list with all producers")```: Serve para facilitar a descrição do teste.
* ```@Mock```: Serve para dar o comportamento de mock para o objeto, pois se não der comportamento será ```null```.
* ```@Order(n)```: Serve para deixar explícito em qual ordem será realizado aquele teste, porém é necessário também adicionar a anotação ```@TestMethodOrder(MethodOrderer.OrderAnnotation.class)``` na classe.

  