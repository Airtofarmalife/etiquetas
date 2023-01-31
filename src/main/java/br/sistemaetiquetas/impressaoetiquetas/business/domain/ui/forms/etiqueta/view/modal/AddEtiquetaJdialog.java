package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@AllArgsConstructor
@Getter
public class AddEtiquetaJdialog extends JDialog {

    private final EtiquetaFormPanel etiquetaFormPanel;
    private final EtiquetaFormBtnPanel etiquetaFormBtnPanel;
    private final EtiquetaAnexoPanel etiquetaAnexoPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.ETIQUETA_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(550,500);
        setPreferredSize(new Dimension(550,500));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        customizeCloseOperation();
    }

    private void initComponents() {
        add(etiquetaAnexoPanel,BorderLayout.WEST);
        add(etiquetaFormPanel, BorderLayout.CENTER);
        add(etiquetaFormBtnPanel, BorderLayout.SOUTH);
        etiquetaAnexoPanel.setVisible(false);
    }

    private void customizeCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                etiquetaFormPanel.clearForm();
            }
        });
    }
}
