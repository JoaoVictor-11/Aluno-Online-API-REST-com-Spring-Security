package br.com.alunoonline.api.infra.security;

/**
 * DTO para retorno do token JWT gerado após o login com sucesso.
 */
public record DadosTokenJWT(String token) {
}
