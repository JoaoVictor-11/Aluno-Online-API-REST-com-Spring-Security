package br.com.alunoonline.api.dtos;

/**
 * DTO contendo credenciais recebidas na requisição de autenticação.
 */
public record DadosAutenticacao(String login, String senha) {
}
