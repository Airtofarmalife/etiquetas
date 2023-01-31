package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.listagem;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.model.UsuarioTableModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

@Getter
@Component
public class ListarPainelTabelaEsterelizacao extends JPanel {

    private final UsuarioTableModel tableModel;

    private JTable table;

    ListarPainelTabelaEsterelizacao(UsuarioTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {
        setLayout(new BorderLayout());
    }

    private void initComponents() {

        table = new JTable(tableModel);

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane paneWithTable = new JScrollPane(table);
        add(paneWithTable, BorderLayout.CENTER);
    }

}
