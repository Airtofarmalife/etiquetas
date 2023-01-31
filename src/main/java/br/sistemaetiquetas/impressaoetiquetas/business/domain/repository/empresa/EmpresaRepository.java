package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.empresa;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface EmpresaRepository extends CrudRepository<Empresa,Integer> {


}