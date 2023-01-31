package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    List<Usuario> findByUsuariosType(EnumUsuariosType usuariosType);

    void remove(Usuario usuario);

    Usuario findByNomeAndUsuariosTypeAndNomeComplementar(String nome, EnumUsuariosType enumUsuariosType,String nomecomplementar);

    void save(Usuario usuario);
}
