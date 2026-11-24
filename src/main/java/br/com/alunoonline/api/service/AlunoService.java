package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    AlunoRepository alunoRepository;

    /**
     * Salva um novo aluno no banco de dados.
     */
    public void criarAluno(Aluno aluno) {
        alunoRepository.save(aluno);
    }

    /**
     * Retorna a listagem completa de alunos cadastrados.
     */
    public List<Aluno> listarTodosAlunos() {
        return alunoRepository.findAll();
    }

    /**
     * Busca um aluno específico a partir de seu identificador.
     */
    public Optional<Aluno> buscarAlunoPorId(Long id) {
        return alunoRepository.findById(id);
    }

    /**
     * Remove um aluno com base em seu identificador.
     */
    public void deletarAlunoPorId(Long id) {
        alunoRepository.deleteById(id);
    }

    /**
     * Atualiza os dados de um aluno previamente cadastrado.
     */
    public void atualizarAlunoPorId(Long id, Aluno alunoEditado) {
        alunoEditado.setId(id);
        alunoRepository.save(alunoEditado);
    }
}
