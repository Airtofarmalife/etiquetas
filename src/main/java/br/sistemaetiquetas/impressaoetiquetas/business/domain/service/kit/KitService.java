package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.kit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface KitService {

    List<Kit> findAll();

    void remove(Kit kit);

    void save(Kit kit);

    Kit findById(Integer id);

    List<Kit> findAllByOrderByDescricao();

    boolean importar(File arquivo);

}
