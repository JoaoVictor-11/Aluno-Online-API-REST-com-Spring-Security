package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.service.DisciplinaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplinas")
@SecurityRequirement(name = "bearerAuth")
public class DisciplinaController {

    @Autowired
    DisciplinaService disciplinaService;

    /**
     * Endpoint para criação de uma nova disciplina.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarDisciplina(@RequestBody Disciplina disciplina) {
        disciplinaService.criarDisciplina(disciplina);
    }

    /**
     * Endpoint para listagem de todas as disciplinas cadastradas.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> listarTodasDisciplinas() {
        return disciplinaService.listarTodasDisciplinas();
    }

    /**
     * Endpoint para busca de uma disciplina pelo identificador.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Disciplina> buscarDisciplinaPorId(@PathVariable Long id) {
        return disciplinaService.buscarDisciplinaPorId(id);
    }

    /**
     * Endpoint para remoção de uma disciplina por identificador.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarDisciplinaPorId(@PathVariable Long id) {
        disciplinaService.deletarDisciplinaPorId(id);
    }

    /**
     * Endpoint para atualização dos dados de uma disciplina.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarDisciplinaPorId(@PathVariable Long id, @RequestBody Disciplina disciplinaEditada) {
        disciplinaService.atualizarDisciplinaPorId(id, disciplinaEditada);
    }
}
