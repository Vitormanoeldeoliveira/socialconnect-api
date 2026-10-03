package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.validation.EstoqueNaoNegativo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados enviados para cadastrar ou atualizar um produto do estoque")
public record ProdutoRequestDTO(

        @Schema(example = "Arroz 5kg", description = "Nome único do produto", maxLength = 150)
        @NotBlank(message = "{produto.nome.obrigatorio}")
        @Size(max = 150, message = "{produto.nome.tamanho}")
        String nome,

        @Schema(example = "ALIMENTO", description = "Categoria do produto")
        @NotNull(message = "{produto.categoria.obrigatoria}")
        CategoriaProduto categoria,

        @Schema(example = "3", description = "Quantidade atual em estoque", minimum = "0")
        @NotNull(message = "{produto.estoque.obrigatorio}")
        @Min(value = 0, message = "{produto.estoque.minimo}")
        @EstoqueNaoNegativo
        Integer estoqueAtual,

        @Schema(example = "10", description = "Quantidade mínima antes do alerta", minimum = "0")
        @NotNull(message = "{produto.estoque.minimo.obrigatorio}")
        @Min(value = 0, message = "{produto.estoque.minimo}")
        @EstoqueNaoNegativo
        Integer estoqueMinimo,

        @Schema(example = "unidade", description = "Unidade de medida", maxLength = 20)
        @NotBlank(message = "{produto.unidade.obrigatoria}")
        @Size(max = 20, message = "{produto.unidade.tamanho}")
        String unidadeMedida
) {}
