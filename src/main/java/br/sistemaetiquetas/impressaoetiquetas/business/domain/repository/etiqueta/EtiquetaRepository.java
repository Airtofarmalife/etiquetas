package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.etiqueta;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EtiquetaRepository extends JpaRepository<Etiqueta,Integer> {

    // Livre
    @Query(value= "SELECT * " +
            "FROM ETIQUETA " +
            "WHERE HORARIO_GERACAO BETWEEN :dateTimeInicial AND :dateTimeFinal " +
            "ORDER BY HORARIO_GERACAO;", nativeQuery=true)
    List<Etiqueta> findByInterval(LocalDateTime dateTimeInicial, LocalDateTime dateTimeFinal);

    // Por turno
    @Query(value= "SELECT id, id_kit, empresa, nome_kit, cod_barras, tipo, lote, ciclo, esterelizacao, validade, responsavel_esterelizacao ,temperatura " +
            " temperatura_tipo, temperatura_valor, outras, responsavel_preparo, quantidade, observacao, horario_geracao, envolucro, total_envolucro, tipo_envolucro, tamanho  " +
            " FROM RELATORIO " +
            " WHERE TURNO = :turno " +
            " AND FORMATDATETIME(HORARIO_GERACAO,'yyyy-MM-dd') BETWEEN :diaInicial AND :diaFinal", nativeQuery=true)
    List<Etiqueta> findReportData(LocalDate diaInicial, LocalDate diaFinal, String turno);

    // Envolucros
    @Query(value= "SELECT * " +
            "FROM ETIQUETA " +
            "WHERE FORMATDATETIME(HORARIO_GERACAO,'yyyy-MM-dd') BETWEEN :localDateInicial AND :localDateFinal " +
            "AND envolucro IN(:envolucros) " +
            "ORDER BY envolucro;", nativeQuery=true)
    List<Etiqueta> findByIntervalAndEnvolucros(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros);

    @Query(value= "SELECT * " +
            "FROM ETIQUETA " +
            "WHERE FORMATDATETIME(HORARIO_GERACAO,'yyyy-MM-dd') BETWEEN :localDateInicial AND :localDateFinal " +
            "AND envolucro IN(:envolucros) " +
            "AND tipo_envolucro = :tipoEnvolucro " +
            "ORDER BY envolucro;", nativeQuery=true)
    List<Etiqueta> findByIntervalAndEnvolucrosWithType(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros,String tipoEnvolucro);

    @Query(value = "SELECT DISTINCT envolucro FROM ETIQUETA ", nativeQuery = true)
    List<String> findDistinctByEnvolucro();

    @Query(value = "SELECT DISTINCT envolucro FROM ETIQUETA where tipo_envolucro = :tipoEvolucro ", nativeQuery = true)
    List<String> findDistinctByEnvolucroSelecionado(String tipoEvolucro);

}