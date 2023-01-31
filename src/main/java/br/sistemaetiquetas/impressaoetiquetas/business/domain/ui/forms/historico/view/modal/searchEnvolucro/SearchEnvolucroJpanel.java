package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.searchEnvolucro;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.etiqueta.EtiquetaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Collections;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchEnvolucroJpanel extends JPanel {

    private String[] columnNames = {"Envólucro"};
    private DefaultTableModel model;
    private JTable jTable;
    private TableRowSorter<TableModel> rowSorter;
    private JButton selecionarBtn = new JButton("Selecionar");

    @Setter
    private String tipoEnvolucro;

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    private Object[][] envolucros;

    public void loadEnvolucros() {

        List<String> listaEnvolucros;
        if (tipoEnvolucro.trim().length() <= 0) {
            listaEnvolucros = etiquetaRepository.findDistinctByEnvolucro();
        } else {
            listaEnvolucros = etiquetaRepository.findDistinctByEnvolucroSelecionado(tipoEnvolucro);
        }

        listaEnvolucros.removeAll(Collections.singleton(""));

        this.envolucros = new Object[listaEnvolucros.size()][1];
        int i=0;
        for (String envolucro: listaEnvolucros) {
                Object[] arrEtiqueta = {envolucro.trim()};
                this.envolucros[i] = arrEtiqueta;
            i++;
        }
    }


    public void initPanel() {

        loadEnvolucros();

        model =  new DefaultTableModel(envolucros, columnNames);

        jTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        rowSorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(rowSorter);
        jTable.setRowSelectionAllowed(true);
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(selecionarBtn, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);

    }
}