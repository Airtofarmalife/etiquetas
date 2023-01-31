package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.provider;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.exception.JasperReportException;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.facade.JasperLibraryFacade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class JasperProviderAbs implements JasperProvider {

    private static final String PATH_DELIMITER = System.getProperty("file.separator");
    private static final String ROOT_FOLDER_PATH = "C:\\etiquetas\\";

    private final JasperLibraryFacade facade;

    @Value("${jasper.generate-files}")
    private Boolean generateJasperFiles;

    @Getter
    private JasperReport compiledReport;

    public abstract String getAuthor();

    @PostConstruct
    public void postConstruct() {
        try {
            final String reportPath = ROOT_FOLDER_PATH + this.getBasePath() + type().getReport();
            File reportFile = new File(reportPath);
            InputStream targetStream = new FileInputStream(reportFile);
            //final var resource = new ClassPathResource(reportPath);

            compiledReport = facade.compileReport(targetStream, type().name());
            if (Boolean.FALSE.equals(generateJasperFiles)) {
                log.info("Skipping jasper files generation due to jasper.generate-files=false");
                return;
            }

            saveCompiledReport(compiledReport, reportPath);
            compileSubReports();

        } catch (final IOException | InterruptedException ex) {
            log.error("Error compiling {} report {}", type().name(), ex);
            throw new JasperReportException("Error compiling jasper " + type().name() + " report. error=" + ex.getMessage());
        }
    }

    @Override
    public byte[] fromParams(Map<String, Object> params) {
        return fromParamsWithDataSource(params, null);
    }

    @Override
    public JasperPrint generateToScreen(Map<String, Object> params, JRDataSource datasource) {
        if (Objects.isNull(params)) {
            log.error("Error generating jasper {} report with empty params", type().name());
            throw new JasperReportException("Error generating jasper " + type().name() + " report with params=null");
        }

        if (MapUtils.isNotEmpty(getExtraParams())) {
            params.putAll(getExtraParams());
        }

        return fillReportWithDatasource(params, datasource);

    }


    @Override
    public byte[] fromParamsWithDataSource(Map<String, Object> params, JRDataSource datasource) {

        if (Objects.isNull(params)) {
            log.error("Error generating jasper {} report with empty params", type().name());
            throw new JasperReportException("Error generating jasper " + type().name() + " report with params=null");
        }


        if (MapUtils.isNotEmpty(getExtraParams())) {
            params.putAll(getExtraParams());
        }

        final JasperPrint filledPrint = fillReportWithDatasource(params, datasource);

        return facade.exportReport(filledPrint, getAuthor(), type().name());
    }

    protected JasperPrint fillReportWithDatasource(final Map<String, Object> params, JRDataSource datasource) {
        final JRDataSource jrDataSource = Optional.ofNullable(datasource).orElseGet(JREmptyDataSource::new);

        return facade.fillReport(compiledReport, params, jrDataSource, type().name());
    }

    private void saveCompiledReport(final JasperReport compiled, final String filePath) throws IOException, InterruptedException {
        if (Objects.isNull(compiled)) {
            log.error("Error saving compiled jasper {} report with value=null", type().name());
            throw new JasperReportException("Error saving compiled jasper " + type().name() + " report with value=null");
        }
        final String jasperPath = filePath.replaceAll("[.]jrxml", ".jasper");
        File statementsFile = new File( jasperPath);
        if (!statementsFile.exists()) {
            statementsFile.createNewFile();
        }

        facade.saveObject(compiled, statementsFile, type().name());
    }

    private void compileSubReports() throws InterruptedException {
        if (CollectionUtils.isEmpty(this.getSubReports())) {
            return;
        }

        this.getSubReports().parallelStream()
            .map(this.getBasePath()::concat)
            .forEach(this::processSubReport);
    }

    private void processSubReport(String subReport) {
        try {
            final String reportPath = ROOT_FOLDER_PATH + subReport;
            File reportFile = new File(reportPath);
            InputStream targetStream = new FileInputStream(reportFile);
            final JasperReport compiledSubReport = facade.compileReport(targetStream, type().name());
            saveCompiledReport(compiledSubReport, reportFile.getAbsolutePath());
        } catch (IOException | InterruptedException e) {
            log.error("Error compiling subReport {} for report {} - Error={}", subReport, type().name(), e);
            throw new JasperReportException("Error compiling jasper subReport " + subReport + " report. Error=" + e.getMessage());
        }
    }
}
