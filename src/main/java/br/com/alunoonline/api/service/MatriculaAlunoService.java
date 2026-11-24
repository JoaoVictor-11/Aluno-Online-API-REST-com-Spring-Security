package br.com.alunoonline.api.service;

import br.com.alunoonline.api.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.dtos.AtualizarNotasRequestDTO;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatriculaAlunoService {

    @Autowired
    MatriculaAlunoRepository matriculaAlunoRepository;

    private static final Double MEDIA_PARA_APROVACAO = 7.0;

    /**
     * Atualiza as notas lançadas e recalcula a situação acadêmica do aluno caso ambas estejam preenchidas.
     */
    public void atualizarNotas(Long id, AtualizarNotasRequestDTO dto) {
        // Busca a matrícula existente ou lança 404 se não for encontrada
        MatriculaAluno matricula = matriculaAlunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        // Aplica atualização incremental apenas nos campos informados na requisição
        if (dto.getNota1() != null) {
            matricula.setNota1(dto.getNota1());
        }
        if (dto.getNota2() != null) {
            matricula.setNota2(dto.getNota2());
        }

        // Calcula a média e define a situação acadêmica quando as duas notas estiverem disponíveis
        if (matricula.getNota1() != null && matricula.getNota2() != null) {
            Double media = (matricula.getNota1() + matricula.getNota2()) / 2;
            matricula.setStatus(media >= MEDIA_PARA_APROVACAO
                    ? MatriculaAlunoStatusEnum.APROVADO
                    : MatriculaAlunoStatusEnum.REPROVADO);
        }

        // Persiste as alterações no banco de dados
        matriculaAlunoRepository.save(matricula);
    }

    /**
     * Registra uma nova matrícula definindo a situação inicial como MATRICULADO.
     */
    public void criarMatricula(MatriculaAluno matriculaAluno) {
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        matriculaAlunoRepository.save(matriculaAluno);
    }

    /**
     * Altera o status da matrícula para TRANCADO, permitindo a operação somente para matrículas com status MATRICULADO.
     */
    public void trancarMatricula(Long id) {
        // Localiza a matrícula para validação prévia
        MatriculaAluno matricula = matriculaAlunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Valida se o status atual permite o trancamento
        if (matricula.getStatus().equals(MatriculaAlunoStatusEnum.MATRICULADO)) {
            matricula.setStatus(MatriculaAlunoStatusEnum.TRANCADO);
            matriculaAlunoRepository.save(matricula);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Só é possível trancar com status MATRICULADO");
        }
    }
}
