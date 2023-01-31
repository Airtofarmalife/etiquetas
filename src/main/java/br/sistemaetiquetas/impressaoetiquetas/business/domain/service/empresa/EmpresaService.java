package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;

import java.util.List;

public interface EmpresaService {

    List<Empresa> findAll();

    void remove(Empresa empresa);

    void save(Empresa empresa);

    Empresa findById(Integer id);

}
