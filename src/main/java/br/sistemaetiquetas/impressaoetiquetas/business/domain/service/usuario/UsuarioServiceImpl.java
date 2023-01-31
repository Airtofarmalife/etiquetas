package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.usuario.UsuarioRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() { return usuarioRepository.findAll(); }

    @Override
    public Usuario findByNomeAndUsuariosTypeAndNomeComplementar(String nome, EnumUsuariosType enumUsuariosType,String nomeComplementar) throws IncorrectResultSizeDataAccessException {
        return usuarioRepository.findByNomeAndUsuariosTypeAndNomeComplementar(nome,enumUsuariosType,nomeComplementar);
    }

    @Override
    public List<Usuario> findByUsuariosType(EnumUsuariosType usuariosType) {
        return usuarioRepository.findByUsuariosType(usuariosType);
    }

    @Override
    public void remove(Usuario usuario) { usuarioRepository.delete(usuario); }

    @Override
    public void save(Usuario usuario) { usuarioRepository.save(usuario);}

}
