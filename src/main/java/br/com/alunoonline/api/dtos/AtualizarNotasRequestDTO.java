package br.com.alunoonline.api.dtos;

import lombok.Data;

/**
 * DTO para atualização parcial de notas de uma matrícula.
 */
@Data
public class AtualizarNotasRequestDTO {
    private Double nota1;
    private Double nota2;
}
