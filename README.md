# SocialConnect API

> API RESTful de gestão para instituições sociais (ONGs, bancos de alimentos,
> CRAS, abrigos). Conecta doadores, voluntários e beneficiários.

**Disciplina:** Tópicos Especiais em Sistemas para Internet III
**Stack:** Java 21 · Spring Boot 4.1.1 · JPA · H2 (dev) · PostgreSQL (prod)

---

## Como Rodar

### Pré-requisitos

- JDK 21 LTS ([Adoptium](https://adoptium.net/))
- Maven 3.9+ (ou use o wrapper: `./mvnw`)
- IDE: IntelliJ IDEA (recomendado) ou VS Code

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd socialconnect-api

# 2. Compile o projeto
mvn clean compile

# 3. Rode a aplicação
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. O Swagger fica em `http://localhost:8080/swagger-ui.html`.

O banco de desenvolvimento é o PostgreSQL local, database `socialconnect`, em `localhost:5432`. As tabelas são criadas pelo Flyway na subida da aplicação.

## Módulo de Produtos

Controla o estoque de doações físicas (alimentos, roupas, higiene e outros).

| Método | Endpoint | Sucesso |
|--------|----------|---------|
| GET | `/api/v1/produtos?page=0&size=10&sort=nome,asc&nome=arroz&categoria=ALIMENTO` | 200 |
| GET | `/api/v1/produtos/{idProduto}` | 200 |
| POST | `/api/v1/produtos` | 201 + header `Location` |
| PUT | `/api/v1/produtos/{idProduto}` | 200 |
| DELETE | `/api/v1/produtos/{idProduto}` | 204 |

Regras:

- Nome do produto é único. Repetir o nome responde `409 Conflict`.
- Estoque atual e estoque mínimo não podem ser negativos. Nesse caso a API responde `422 Unprocessable Entity`.
- A resposta inclui `estoqueBaixo: true` quando `estoqueAtual` é menor que `estoqueMinimo`.
- Produto inexistente responde `404 Not Found` no formato Problem Details.

Exemplo de cadastro:

```json
{
  "nome": "Arroz 5kg",
  "categoria": "ALIMENTO",
  "estoqueAtual": 3,
  "estoqueMinimo": 10,
  "unidadeMedida": "unidade"
}
```

Categorias aceitas: `ALIMENTO`, `ROUPA`, `HIGIENE`, `OUTROS`.

## Testes

```bash
mvn test
```

O teste unitário `ProdutoServiceTest` usa Mockito e não precisa de banco.

O teste de integração `ProdutoControllerIntegrationTest` sobe um PostgreSQL 17 temporário com Testcontainers. Quem for executar esse teste precisa do Docker ligado. A aplicação em si continua no PostgreSQL local (`localhost:5432`, database `socialconnect`).