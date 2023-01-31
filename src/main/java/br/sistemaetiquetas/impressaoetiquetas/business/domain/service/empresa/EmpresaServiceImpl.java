package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.empresa.EmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> findAll() {
        return (List<Empresa>) empresaRepository.findAll();
    }

    @Override
    public void remove(Empresa empresa) { empresaRepository.delete(empresa);}

    @Override
    public void save(Empresa empresa) { empresaRepository.save(empresa);}

    @Override
    public Empresa findById(Integer id) { return empresaRepository.findById(id).orElse(null);}

}
