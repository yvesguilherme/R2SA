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