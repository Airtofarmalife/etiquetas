package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.search;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.kit.KitRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ListKitJpanel extends JPanel {

    private String[] columnNames = {"ID","Descrição", "Cód. Barras", "Quantidade","Tamanho","Tipo Envólucro","Envólucro","Qnde Envólucro"};
    private DefaultTableModel model;
    private JTable jTable;
    private TableRowSorter<TableModel> rowSorter;
    @Setter
    private JTextField jtfFilter = new JTextField();
    private JButton ediBtn = new JButton(ConstMessages.Labels.BOTAO_EDITAR);
    private JButton removeBtn = new JButton(ConstMessages.Labels.BOTAO_REMOVER);
    private JButton addBtn = new JButton(ConstMessages.Labels.ADD_BTN);

    @Autowired
    private KitRepository kitRepository;

    private Object[][] kits;

    public void loadKits() {
        List<Kit> listakits = (List<Kit>) kitRepository.findAllByOrderByDescricao();
        this.kits = new Object[listakits.size()][2];
        int i=0;
        for (Kit kit: listakits) {
            Object[] arrKit = {
                    kit.getId(),
                    kit.getDescricao(),
                    kit.getCodbarras(),
                    kit.getQuantidade(),
                    kit.getTamanho(),
                    kit.getTipoEnvolucro(),
                    kit.getEnvolucro(),
                    kit.getTotalEnvolucro()};
            this.kits[i] = arrKit;
            i++;
        }
    }


    public void initPanel() {

        loadKits();
        model = new DefaultTableModel(kits, columnNames);

        jTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        rowSorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(rowSorter);

        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(290);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(115);
        jTable.getColumnModel().getColumn(3).setPreferredWidth(115);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(115);
        jTable.getColumnModel().getColumn(5).setPreferredWidth(125);
        jTable.getColumnModel().getColumn(6).setPreferredWidth(115);
        jTable.getColumnModel().getColumn(7).setPreferredWidth(140);


//        ((DefaultTableCellRenderer) jTable.getTableHeader().getDefaultRenderer())
//                .setHorizontalAlignment(JLabel.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JLabel("Pesquisar:"));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(jtfFilter);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(ediBtn);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(addBtn);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(removeBtn);

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(jTable), BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);

        jtfFilter.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text,1));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text,1));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }
}