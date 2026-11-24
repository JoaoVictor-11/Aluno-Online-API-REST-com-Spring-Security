package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    /**
     * Salva um novo professor no banco de dados.
     */
    public void criarProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    /**
     * Retorna a lista com todos os professores cadastrados.
     */
    public List<Professor> listarTodosProfessores() {
        return professorRepository.findAll();
    }

    /**
     * Localiza um professor pelo identificador.
     */
    public Optional<Professor> buscarProfessorPorId(Long id) {
        return professorRepository.findById(id);
    }

    /**
     * Remove um professor pelo identificador.
     */
    public void deletarProfessorPorId(Long id) {
        professorRepository.deleteById(id);
    }

    /**
     * Atualiza os dados de um professor existente.
     */
    public void atualizarProfessorPorId(Long id, Professor professorEditado) {
        professorEditado.setId(id);
        professorRepository.save(professorEditado);
    }
}
