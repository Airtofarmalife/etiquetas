package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.model.PeriodoTableModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Getter
@Component
public class PeriodoTablePanel extends JPanel {

    private final PeriodoTableModel tableModel;

    private JTable table;

    PeriodoTablePanel(PeriodoTableModel tableModel) {
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane paneWithTable = new JScrollPane(table);
        add(paneWithTable, BorderLayout.CENTER);
    }

}
