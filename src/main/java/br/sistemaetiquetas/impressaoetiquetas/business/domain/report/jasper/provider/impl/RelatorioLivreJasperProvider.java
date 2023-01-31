package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.provider.impl;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration.JasperReportType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.facade.JasperLibraryFacade;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.provider.JasperProviderAbs;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RelatorioLivreJasperProvider extends JasperProviderAbs {

    @Getter
    @Value("${jasper.default-author}")
    private String author;

    private final List<String> subReports = super.getSubReports();
    private final Map<String, Object> extraParams = super.getExtraParams();

    public RelatorioLivreJasperProvider(final JasperLibraryFacade facade) {
        super(facade);
    }

    @Override
    public JasperReportType type() {
        return JasperReportType.RELATORIO_LIVRE;
    }

    @Override
    public List<String> getSubReports() {
        if (!subReports.isEmpty()) {
            return subReports;
        }

        subReports.add("subreport_livre.jrxml");

        return subReports;
    }

    @Override
    public Map<String, Object> getExtraParams() {
        if (!extraParams.isEmpty()) {
            return extraParams;
        }

        extraParams.put("BASE_PATH", this.getBasePath());
        extraParams.put("STATEMENTS_SUBREPORT_PATH", "subreport_livre.jasper");

        return extraParams;
    }
}
