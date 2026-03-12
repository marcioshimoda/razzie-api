# Golden Raspberry Awards API

API RESTful para leitura da lista de indicados e vencedores da categoria
**Worst Picture** do Golden Raspberry Awards (Razzie Awards).

A aplicação importa os dados de um arquivo CSV e disponibiliza um
endpoint que retorna os produtores com:

-   **maior intervalo entre dois prêmios consecutivos**
-   **menor intervalo entre dois prêmios consecutivos**

------------------------------------------------------------------------

# Tecnologias utilizadas

-   Java 25 (compilando para 17)
-   Spring Boot 3.5
-   Spring Data JPA
-   H2 Database (em memória)
-   Maven
-   Lombok
-   JUnit (testes de integração)

------------------------------------------------------------------------

# Arquitetura

A aplicação segue uma arquitetura simples baseada em camadas:

Controller → Service → Repository → Database (H2)

Responsabilidades:

**Controller** - exposição dos endpoints REST

**Service** - regras de negócio e cálculo dos intervalos

**Repository** - acesso aos dados com Spring Data JPA

**Loader** - importação automática do CSV na inicialização

------------------------------------------------------------------------

# Execução do projeto

## Pré-requisitos

-   Java 17+ (recomendado Java 25)
-   Maven 3.8+

Verificar instalação:

``` bash
java -version
mvn -v
```

------------------------------------------------------------------------

## Rodar a aplicação

Na raiz do projeto:

``` bash
./mvnw spring-boot:run
```

ou

``` bash
mvn spring-boot:run
```

A aplicação iniciará em:

http://localhost:8080

------------------------------------------------------------------------

# Endpoint da API

## Obter intervalo de prêmios dos produtores

    GET /producers/awards/intervals

Exemplo de resposta:

``` json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

------------------------------------------------------------------------

# Banco de dados

A aplicação utiliza **H2 em memória**.

Console disponível em:

http://localhost:8080/h2-console

Configuração:

    JDBC URL: jdbc:h2:mem:razzie
    User: sa
    Password:

------------------------------------------------------------------------

# Importação dos dados

Na inicialização da aplicação os dados são carregados automaticamente do
arquivo:

    src/main/resources/Movielist.csv

O nome do arquivo está configurado na razzie.csv.file da application.properties.

Também é possível informar um arquivo externo:

``` bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--razzie.csv.file=/path/movies.csv"
```

------------------------------------------------------------------------

# Testes de integração

Executar:

``` bash
./mvnw test
```

Os testes garantem que os resultados retornados pela API estão de acordo
com os dados fornecidos.

------------------------------------------------------------------------

# Requisitos atendidos

✔ API REST nível 2 de maturidade de Richardson\
✔ Banco de dados em memória (H2)\
✔ Importação automática de CSV\
✔ Testes de integração\
✔ Execução utilizando outros conjuntos de dados\
✔ Projeto autossuficiente (sem dependências externas)

------------------------------------------------------------------------

# Autor

Marcio Shimoda
