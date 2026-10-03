package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.NomeDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        boolean filtrarNome = nome != null && !nome.isBlank();
        boolean filtrarCategoria = categoria != null;

        Page<Produto> page;
        if (filtrarNome && filtrarCategoria) {
            page = repository.findByNomeContainingIgnoreCaseAndCategoria(nome, categoria, pageable);
        } else if (filtrarNome) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (filtrarCategoria) {
            page = repository.findByCategoria(categoria, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidade(id));
    }

    @Override
    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarRegras(dto);

        if (repository.existsByNome(dto.nome())) {
            throw new NomeDuplicadoException(dto.nome());
        }

        Produto produto = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        return toResponseDTO(repository.save(produto));
    }

    @Override
    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        validarRegras(dto);

        Produto produto = buscarEntidade(id);

        if (repository.existsByNomeAndIdProdutoNot(dto.nome(), id)) {
            throw new NomeDuplicadoException(dto.nome());
        }

        produto.setNome(dto.nome());
        produto.setCategoria(dto.categoria());
        produto.setEstoqueAtual(dto.estoqueAtual());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setUnidadeMedida(dto.unidadeMedida());

        return toResponseDTO(repository.save(produto));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private Produto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    private void validarRegras(ProdutoRequestDTO dto) {
        if (dto.estoqueAtual() != null && dto.estoqueAtual() < 0) {
            throw new EstoqueNegativoException("O estoque atual não pode ser negativo.");
        }
        if (dto.estoqueMinimo() != null && dto.estoqueMinimo() < 0) {
            throw new EstoqueNegativoException("O estoque mínimo não pode ser negativo.");
        }
    }

    private ProdutoResponseDTO toResponseDTO(Produto entity) {
        boolean estoqueBaixo = entity.getEstoqueAtual() < entity.getEstoqueMinimo();
        return new ProdutoResponseDTO(
                entity.getIdProduto(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getEstoqueAtual(),
                entity.getEstoqueMinimo(),
                entity.getUnidadeMedida(),
                entity.getDataCadastro(),
                estoqueBaixo
        );
    }
}
