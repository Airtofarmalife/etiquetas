package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.jasper;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration.JasperReportType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.exception.MissingJasperProviderException;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.provider.JasperProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdfService {

    private final List<JasperProvider> providers;

    public JasperPrint generateToScreen(final JasperReportType reportType, final Map<String, Object> params) {
        log.info("Creating file for ReportType: {}", reportType.name());
            return getProviderFromType(reportType)
                .generateToScreen(params, null);
    }


    public byte[] generate(final JasperReportType reportType, final Map<String, Object> params) {
        log.info("Creating file for ReportType: {}", reportType.name());

        return generateWithDataSource(reportType, params, null);
    }

    public byte[] generateWithDataSource(final JasperReportType reportType, final Map<String, Object> params, JRDataSource datasource) {
        log.info("Creating file for Report with datasource={} for ReportType={}", datasource, reportType.name());

        return getProviderFromType(reportType)
            .fromParamsWithDataSource(params, datasource);
    }

    private JasperProvider getProviderFromType(final JasperReportType type) {
        return providers.stream()
            .filter(it -> type.equals(it.type()))
            .findFirst()
            .orElseThrow(MissingJasperProviderException::new);
    }
}
