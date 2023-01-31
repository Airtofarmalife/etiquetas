package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.periodo;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.periodo.PeriodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PeriodoServiceImpl implements PeriodoService {

    private final PeriodoRepository periodoRepository;

    @Override
    public List<Periodo> findAll() { return periodoRepository.findAll(); }

    @Override
    public void remove(Periodo periodo) { periodoRepository.delete(periodo);}

    @Override
    public void save(Periodo periodo) { periodoRepository.save(periodo);}

    @Override
    public Periodo findByNome(String nome) { return  periodoRepository.findByNome(nome); }
}
