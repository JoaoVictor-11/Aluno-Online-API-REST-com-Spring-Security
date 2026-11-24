package br.com.alunoonline.api.dtos;

import br.com.alunoonline.api.model.Usuario;

/**
 * DTO para retorno dos dados cadastrais do usuário sem expor a senha.
 */
public record DadosDetalhamentoUsuario(Long id, String login) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin());
    }
}
