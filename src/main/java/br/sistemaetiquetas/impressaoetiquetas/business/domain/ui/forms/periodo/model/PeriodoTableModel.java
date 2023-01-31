package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.model;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.model.DefaultTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class PeriodoTableModel extends DefaultTableModel<Periodo> {

    private static final int NOME_INDEX = 0;
    private static final int HORA_INICIAL_INDEX = 1;
    private static final int HORA_FINAL_INDEX = 2;

    @Override
    public String[] getColumnLabels() {
        return new String[]{
            ConstMessages.Labels.PERIODO_NOME,
            ConstMessages.Labels.PERIODO_HORA_INICIAL,
            ConstMessages.Labels.PERIODO_HORA_FINAL
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Periodo periodo = entities.get(rowIndex);

        switch (columnIndex) {
            case NOME_INDEX:
                return periodo.getNome();
            case HORA_INICIAL_INDEX:
                return periodo.getHoraInicial().toString();
            case HORA_FINAL_INDEX:
                return periodo.getHoraFinal().toString();
            default:
                return Strings.EMPTY;
        }
    }

}
