package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.facade;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface JasperLibraryFacade {

    JasperReport compileReport(InputStream inputStream, String reportName);

    JasperPrint fillReport(JasperReport compiledReport, Map<String, Object> params, JRDataSource datasource, String reportName);

    void saveObject(Object obj, File file, String reportName);

    byte[] exportReport(JasperPrint jasperPrint, String author, String reportName);
}
