package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.provider;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration.JasperReportType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JasperProvider {

    JasperReportType type();

    byte[] fromParams(Map<String, Object> params);

    byte[] fromParamsWithDataSource(Map<String, Object> params, JRDataSource datasource);

    JasperReport getCompiledReport();

    JasperPrint generateToScreen(Map<String, Object> params, JRDataSource datasource);

    default Map<String, Object> getExtraParams() {
        return new HashMap<>();
    }

    default List<String> getSubReports() {
        return new ArrayList<>();
    }

    default String getBasePath() {
        return type().getBasePath();
    }
}
