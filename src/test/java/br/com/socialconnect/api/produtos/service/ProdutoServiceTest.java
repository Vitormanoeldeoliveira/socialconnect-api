package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.NomeDuplicadoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoServiceImpl service;

    @Test
    @DisplayName("Deve criar produto quando os dados são válidos")
    void deveCriarProdutoQuandoDadosValidos() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );

        Mockito.when(repository.existsByNome("Arroz 5kg")).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Produto.class))).thenAnswer(invocation -> {
            Produto produto = invocation.getArgument(0);
            produto.setIdProduto(1L);
            return produto;
        });

        // Act
        ProdutoResponseDTO resultado = service.criar(dto);

        // Assert
        Assertions.assertEquals(1L, resultado.idProduto());
        Assertions.assertEquals("Arroz 5kg", resultado.nome());
        Assertions.assertEquals(CategoriaProduto.ALIMENTO, resultado.categoria());
        Assertions.assertEquals(3, resultado.estoqueAtual());
        Assertions.assertEquals(10, resultado.estoqueMinimo());
        Assertions.assertTrue(resultado.estoqueBaixo());
        Mockito.verify(repository).save(Mockito.any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o estoque é negativo")
    void deveLancarExcecaoQuandoEstoqueNegativo() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Feijão 1kg",
                CategoriaProduto.ALIMENTO,
                -1,
                5,
                "kg"
        );

        // Act + Assert
        Assertions.assertThrows(EstoqueNegativoException.class, () -> service.criar(dto));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome já existe")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // Arrange
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Sabonete",
                CategoriaProduto.HIGIENE,
                20,
                5,
                "unidade"
        );

        Mockito.when(repository.existsByNome("Sabonete")).thenReturn(true);

        // Act + Assert
        Assertions.assertThrows(NomeDuplicadoException.class, () -> service.criar(dto));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Produto.class));
    }
}
