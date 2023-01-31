package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JasperReportType {

    ETIQUETA_STATEMENT("jasper/etiqueta/", "etiqueta.jrxml"),
    RELATORIO_TURNOS("jasper/relatorioTurnos/", "full_statement.jrxml"),
    RELATORIO_ENVOLUCROS("jasper/relatorioEnvolucros/", "envolucros.jrxml"),
    RELATORIO_LIVRE("jasper/relatorioLivre/", "relatorio_livre.jrxml");

    private final String basePath;
    private final String report;
}
