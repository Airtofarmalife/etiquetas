package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.searchkit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.kit.KitRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchKitJpanel extends JPanel {

    private String[] columnNames = {"ID","Descrição", "Cód. Barras", "Quantidade","Tamanho","Tipo Envólucro","Envólucro","Qnde Envólucro"};
    private DefaultTableModel model;
    private JTable jTable;
    private TableRowSorter<TableModel> rowSorter;
    private JTextField jtfFilter = new JTextField();
    private JButton jbtFilter = new JButton("Filter");
    private JButton selecionarBtn = new JButton("Selecionar");

    @Autowired
    private KitRepository kitRepository;

    private Object[][] kits;

    public void loadKits() {
        List<Kit> listakits = (List<Kit>) kitRepository.findAll();
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
                    kit.getTotalEnvolucro()
            };
            this.kits[i] = arrKit;
            i++;
        }
    }


    public void initPanel() {

        loadKits();

        model =  new DefaultTableModel(kits, columnNames);

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

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Pesquisar:"), BorderLayout.WEST);
        panel.add(jtfFilter, BorderLayout.CENTER);
        panel.add(selecionarBtn, BorderLayout.EAST);

         /*
        *  JLabel pesquisarLbl = new JLabel("Pesquisar:");
        pesquisarLbl.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(pesquisarLbl, BorderLayout.WEST);
        jtfFilter.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(jtfFilter, BorderLayout.CENTER);
        selecionarBtn.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(selecionarBtn, BorderLayout.EAST);
        *
        * */

        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);

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