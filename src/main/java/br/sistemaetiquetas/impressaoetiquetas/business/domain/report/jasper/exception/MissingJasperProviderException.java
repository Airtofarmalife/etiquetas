package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.exception;

public class MissingJasperProviderException extends RuntimeException {

    public MissingJasperProviderException() {
        super("Não foi possível gerar o tipo de relatório requisitado no momento, tente novamente mais tarde.");
    }
}
