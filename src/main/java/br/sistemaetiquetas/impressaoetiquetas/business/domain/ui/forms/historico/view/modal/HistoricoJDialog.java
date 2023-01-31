package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal;

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
public class HistoricoJDialog extends JDialog {

    private final HistoricoFormPanel historicoFormPanel;
    private final HistoricoFormBtnPanel historicoFormBtnPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.HISTORICO_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500,530);
        setPreferredSize(new Dimension(500,530));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        customizeCloseOperation();
    }

    private void initComponents() {
        add(historicoFormPanel, BorderLayout.CENTER);
        add(historicoFormBtnPanel, BorderLayout.SOUTH);
    }

    private void customizeCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                historicoFormPanel.clearForm();
            }
        });
    }
}
