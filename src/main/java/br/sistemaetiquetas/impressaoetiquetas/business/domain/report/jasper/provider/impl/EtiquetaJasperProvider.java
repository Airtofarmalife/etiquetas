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
public class EtiquetaJasperProvider extends JasperProviderAbs {

    @Getter
    @Value("${jasper.default-author}")
    private String author;

    private final Map<String, Object> extraParams = super.getExtraParams();

    public EtiquetaJasperProvider(final JasperLibraryFacade facade) {
        super(facade);
    }

    @Override
    public JasperReportType type() {
        return JasperReportType.ETIQUETA_STATEMENT;
    }

    @Override
    public Map<String, Object> getExtraParams() {
        if (!extraParams.isEmpty()) {
            return extraParams;
        }
        extraParams.put("BASE_PATH", this.getBasePath());
        return extraParams;
    }
}
