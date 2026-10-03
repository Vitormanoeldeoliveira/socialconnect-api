package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
class ProdutoControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configurarPostgresDoContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.h2.console.enabled", () -> "false");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void limparProdutos() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("POST - Deve criar produto e retornar 201")
    void deveCriarProdutoQuandoDadosValidos() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );

        // Act
        ResponseEntity<ProdutoResponseDTO> response = restTemplate.postForEntity(
                "/api/v1/produtos", dto, ProdutoResponseDTO.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/api/v1/produtos/");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().idProduto()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("Arroz 5kg");
        assertThat(response.getBody().estoqueBaixo()).isTrue();
    }

    @Test
    @DisplayName("POST - Deve retornar 409 quando o nome já existe")
    void deveRetornar409QuandoNomeDuplicado() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Sabonete",
                CategoriaProduto.HIGIENE,
                20,
                5,
                "unidade"
        );
        restTemplate.postForEntity("/api/v1/produtos", dto, ProdutoResponseDTO.class);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/produtos", dto, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("Nome já cadastrado");
    }

    @Test
    @DisplayName("POST - Deve retornar 422 quando o estoque é negativo")
    void deveRetornar422QuandoEstoqueNegativo() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Feijão 1kg",
                CategoriaProduto.ALIMENTO,
                -1,
                5,
                "kg"
        );

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/produtos", dto, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("estoque-negativo");
    }
}
