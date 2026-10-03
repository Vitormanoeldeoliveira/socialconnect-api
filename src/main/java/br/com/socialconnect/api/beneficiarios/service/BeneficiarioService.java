package br.com.socialconnect.api.beneficiarios.service;

import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioRequestDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioResponseDTO;
import br.com.socialconnect.api.beneficiarios.model.Beneficiario;
import br.com.socialconnect.api.beneficiarios.repository.BeneficiarioRepository;
import br.com.socialconnect.api.exception.CpfDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BeneficiarioService {

    private final BeneficiarioRepository repository;

    public BeneficiarioService(BeneficiarioRepository repository) {
        this.repository = repository;
    }

    // ✅ O nome do método deve ser EXATAMENTE "listar"
    public Page<BeneficiarioResponseDTO> listar(String nome, String cpf, Pageable pageable) {
        Page<Beneficiario> page;

        if (cpf != null && !cpf.isBlank()) {
            page = repository.findByCpf(cpf, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    public BeneficiarioResponseDTO buscarPorId(Long idBeneficiario) {
        return repository.findById(idBeneficiario)
                .map(this::toResponseDTO) // (ou o mapeamento que você usa aí)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Beneficiário não encontrado: " + idBeneficiario));
    }

    public BeneficiarioResponseDTO criar(BeneficiarioRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new CpfDuplicadoException(dto.cpf());
        }

        Beneficiario entity = Beneficiario.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .situacaoVulnerabilidade(dto.situacaoVulnerabilidade())
                .dataCadastro(LocalDate.now())
                .build();

        return toResponseDTO(repository.save(entity));
    }

    public void deletar(Long idBeneficiario) {
        if (!repository.existsById(idBeneficiario)) {
            throw new RecursoNaoEncontradoException("Beneficiário não encontrado: " + idBeneficiario);
        }
        repository.deleteById(idBeneficiario);
    }

    // Mapeador Entity -> Response DTO
    private BeneficiarioResponseDTO toResponseDTO(Beneficiario entity) {
        return new BeneficiarioResponseDTO(
                entity.getIdBeneficiario(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getEndereco(),
                entity.getSituacaoVulnerabilidade(),
                entity.getDataCadastro()
        );
    }
}