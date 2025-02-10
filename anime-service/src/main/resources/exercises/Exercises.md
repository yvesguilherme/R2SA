# Exercises

###  1 - GET

<hr />

* [x] Create a controller and a method that returns a list of anime names through the following URL: http://localhost:8080/v1/animes

### 2 - @RequestParam, @PathVariable

<hr />

* [x] Crie uma classe chamada Anime, dentro de um pacote chamado domain, com os seguintes atributos:
  * **long id**;
  * **String name**;
* [x] Crie um método dentro de Anime que retorne uma Lista "**hardcoded**" de Animes;
* [x] Atualize o AnimeController para retornar uma lista de Anime, em seguida, crie outros dois métodos:
  * Um para filtrar pelo nome, usando **@RequestParam**;
  * Outro para retornar um Anime pelo id, usando o **@PathVariable**

### 3 - POST

<hr />

* [x] Atualize o AnimeController através da adição de um método que recebe um Anime JSON via POST
* [x] Adiciona na lista existente de animes e retorna o objeto criado. Restrição: Caso o id seja passado na requisição, ele deve ser ignorado.
* [x] Gere um id aleatoriamente antes de adicionar o objeto na lista