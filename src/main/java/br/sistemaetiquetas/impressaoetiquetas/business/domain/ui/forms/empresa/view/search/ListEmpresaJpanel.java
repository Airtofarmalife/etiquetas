package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.search;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.empresa.EmpresaRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ListEmpresaJpanel extends JPanel {

    private String[] columnNames = {"id","Nome","Validade"};
    private DefaultTableModel model;
    private JTable jTable;
    private TableRowSorter<TableModel> rowSorter;
    private JButton ediBtn = new JButton(ConstMessages.Labels.BOTAO_EDITAR);
    private JButton removeBtn = new JButton(ConstMessages.Labels.BOTAO_REMOVER);
    private JButton addBtn = new JButton(ConstMessages.Labels.ADD_BTN);

    @Autowired
    private EmpresaRepository empresaRepository;

    private Object[][] empresas;

    public void loadEmpresas() {
        List<Empresa> listaEmpresa = (List<Empresa>) empresaRepository.findAll();
        this.empresas = new Object[listaEmpresa.size()][2];

        int i=0;
        LocalDate dataValidacao;
        for (Empresa empresa: listaEmpresa) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Object[] arrEmpresas = {
                    empresa.getId(),
                    empresa.getNome(),
                    empresa.getValidade().format(formatter),
            };
            this.empresas[i] = arrEmpresas;
            i++;
        }
    }


    public void initPanel() {

        loadEmpresas();
        model = new DefaultTableModel(empresas, columnNames);

        jTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        rowSorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(rowSorter);

        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(290);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(110);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(ediBtn);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(addBtn);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(removeBtn);

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(jTable), BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);

        }
}