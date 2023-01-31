package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.model;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.model.DefaultTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class UsuarioTableModel extends DefaultTableModel<Usuario> {

    private static final int NOME_INDEX = 0;
    private static final int NOME_COMPLEMENTAR_INDEX = 1;

    @Override
    public String[] getColumnLabels() {
        return new String[]{
            ConstMessages.Labels.USUARIO_NOME,
            ConstMessages.Labels.USUARIO_NOME_COMPLEMENTAR
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario usuario = entities.get(rowIndex);

        switch (columnIndex) {
            case NOME_INDEX:
                return usuario.getNome();
            case NOME_COMPLEMENTAR_INDEX:
                return usuario.getNomeComplementar();
            default:
                return Strings.EMPTY;
        }
    }

}
