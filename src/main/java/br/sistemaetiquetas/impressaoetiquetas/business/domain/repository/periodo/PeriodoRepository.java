package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.periodo;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo,Integer> {
    Periodo findByNome(String nome);
}