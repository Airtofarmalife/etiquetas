package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.facade;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.exception.JasperReportException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static net.sf.jasperreports.export.type.PdfPermissionsEnum.PRINTING;

@Slf4j
@Component
public class JasperLibraryFacadeImpl implements JasperLibraryFacade {

    @Override
    public JasperReport compileReport(final InputStream inputStream, final String reportName) {
        try {
            return JasperCompileManager.compileReport(inputStream);
        } catch (JRException e) {
            log.error("Error compiling {} report {}", reportName, e);
            throw new JasperReportException("Error compiling jasper " + reportName
                + " report. error=" + e.getMessage());
        }
    }

    @Override
    public JasperPrint fillReport(final JasperReport compiledReport, final Map<String, Object> params,
        final JRDataSource datasource, final String reportName) {
        try {
            return JasperFillManager.fillReport(compiledReport,
                params,
                Optional.ofNullable(datasource).orElseGet(JREmptyDataSource::new)
            );
        } catch (final JRException | NullPointerException ex) {
            log.error("Error filling {} report {}", reportName, ex);
            throw new JasperReportException("Error filling jasper " + reportName
                + " report ");
        }
    }

    @Override
    public void saveObject(final Object obj, final File file, final String reportName) {
        try {
            JRSaver.saveObject(obj, file);
        } catch (final JRException ex) {
            log.error("Error saving {} report {}", reportName, ex);
            throw new JasperReportException("Error saving jasper " + reportName
                + " report");
        }
    }

    @Override
    public byte[] exportReport(final JasperPrint jasperPrint, final String author,
        final String reportName) {
        final JRPdfExporter exporter = new JRPdfExporter();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            setupExportReport(jasperPrint, author, exporter, outputStream);

            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (final JRException ex) {
            log.error("Error exporting jasper report {} to PDF {}", reportName, ex);
            throw new JasperReportException("Error exporting jasper report " + reportName + " to OutputStream");
        } catch (final IOException e) {
            log.error("Error closing stream for jasper report {} error={}", reportName, e);
            throw new JasperReportException("Error while closing jasper report stream for report " + reportName);
        }
    }

    private void setupExportReport(final JasperPrint jasperPrint, final String author, final JRPdfExporter exporter,
        final ByteArrayOutputStream outputStream) {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        final SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        final SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(author);
        exportConfig.setAllowedPermissionsHint(PRINTING.getName());

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
    }
}
