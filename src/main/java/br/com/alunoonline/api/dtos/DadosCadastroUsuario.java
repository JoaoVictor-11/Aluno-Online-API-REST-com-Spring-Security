package br.com.alunoonline.api.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para recebimento e validação de dados no cadastro de um novo usuário.
 */
public record DadosCadastroUsuario(
        @NotBlank(message = "Login é obrigatório!")
        String login,

        @NotBlank(message = "Senha é obrigatória!")
        String senha
) {
}
