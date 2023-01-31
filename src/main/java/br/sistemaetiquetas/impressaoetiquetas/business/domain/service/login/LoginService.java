package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;

import java.util.List;

public interface LoginService {

    boolean login(String usuario, String senha);

    boolean verificarCredenciais(String usuario, String senha, String nomeComplementar);

    void logoff();

    String hashPassword(String usuario, String senha);

    List<Usuario> getLogins();

}
