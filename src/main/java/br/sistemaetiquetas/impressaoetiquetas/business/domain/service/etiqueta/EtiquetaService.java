package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.etiqueta;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EtiquetaService {

    List<Etiqueta> findAll();

    void remove(Etiqueta etiqueta);

    void save(Etiqueta etiqueta);

    List<Etiqueta> findByInterval(LocalDateTime dateTimeInicial, LocalDateTime dateTimeFinal);

    List<Etiqueta> findReportData(LocalDate diaInicial, LocalDate diaFinal, String turno);

    List<Etiqueta> findByIntervalAndEnvolucros(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros);

    List<Etiqueta> findByIntervalAndEnvolucrosWithType(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros,String tipoEnvolucro);

    String generateEAN13BarcodeImageBase64(String code) throws Exception;

    List<String> findDistinctByEnvolucro();

    List<String> findDistinctByEnvolucroSelecionado(String envolucro);

}
