package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.periodo;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;

import java.util.List;

public interface PeriodoService {

    List<Periodo> findAll();

    void remove(Periodo periodo);

    void save(Periodo periodo);

    Periodo findByNome(String nome);
}
