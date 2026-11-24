package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.service.ProfessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professores")
@SecurityRequirement(name = "bearerAuth")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    /**
     * Endpoint para cadastro de um novo professor.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarProfessor(@RequestBody Professor professor) {
        professorService.criarProfessor(professor);
    }

    /**
     * Endpoint para recuperação de todos os professores cadastrados.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Professor> listarTodosProfessores() {
        return professorService.listarTodosProfessores();
    }

    /**
     * Endpoint para consulta de um professor pelo identificador.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Professor> buscarProfessorPorId(@PathVariable Long id) {
        return professorService.buscarProfessorPorId(id);
    }

    /**
     * Endpoint para exclusão de um professor por ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProfessorPorId(@PathVariable Long id) {
        professorService.deletarProfessorPorId(id);
    }

    /**
     * Endpoint para atualização dos dados de um professor.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarProfessorPorId(@PathVariable Long id, @RequestBody Professor professorEditado) {
        professorService.atualizarProfessorPorId(id, professorEditado);
    }
}
