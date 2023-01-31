package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.ExtensionFileFilter;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Getter
public class ImportacaoFormPanel extends JPanel {

    private final JLabel importacaoLbl = new JLabel("Selecionar arquivo");
    private final JTextField arquivoExcel = new JTextField(30);

    @PostConstruct
    private void preparePanel() {

        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Importação"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 7 7,insets 10 8 10 8"));
    }

    public void initComponents() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new ExtensionFileFilter(
                new String[] { ".XLS", ".XLSX" },
                "Extensões permitidas (.XLS |.XLSX)"));

        JButton upload = new JButton("Selecionar");
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(getParent());
                File arquivo = fileChooser.getSelectedFile();
                if(arquivo != null) {
                    arquivoExcel.setText(arquivo.getAbsolutePath());
                }
            }
        });

        arquivoExcel.setEditable(false);
        arquivoExcel.setBackground(Color.LIGHT_GRAY);

        add(importacaoLbl,"align label");
        add(arquivoExcel,"split 2");
        add(upload,"wrap");

        repaint();
        revalidate();
    }

    public boolean validateForm() {

        // Arquivo
        if (Strings.isNullOrEmpty(arquivoExcel.getText())) {
            Notifications.showAlertMessage("Nenhum arquivo selecionado");
            return false;
        }

        return true;
    }

    public void clearForm() {
        arquivoExcel.setText("");
    }

}
