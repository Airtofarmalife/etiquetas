package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.model;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.model.DefaultTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class EmpresaTableModel extends DefaultTableModel<Empresa> {

    private static final int NOME_INDEX = 0;
    private static final int VALIDADE_INDEX = 1;

    @Override
    public String[] getColumnLabels() {
        return new String[]{
            ConstMessages.Labels.EMPRESA_NOME,
            ConstMessages.Labels.EMPRESA_VALIDADE
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Empresa empresa = entities.get(rowIndex);

        switch (columnIndex) {
            case NOME_INDEX:
                return empresa.getNome();
            case VALIDADE_INDEX:
                return empresa.getValidade();
            default:
                return Strings.EMPTY;
        }
    }

}
