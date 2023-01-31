package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.usuario;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    List<Usuario> findAll();

    Usuario findByNome(String nome);

    Usuario findByNomeAndUsuariosTypeAndNomeComplementar(String nome, EnumUsuariosType enumUsuariosType, String nomeComplementar);

    List<Usuario> findByUsuariosType(EnumUsuariosType usuariosType);

    @Query(value = "select * from usuario where tipo in ('GESTOR','ADMINISTRADOR','PADRAO')",nativeQuery = true)
    List<Usuario> getLogins();

}