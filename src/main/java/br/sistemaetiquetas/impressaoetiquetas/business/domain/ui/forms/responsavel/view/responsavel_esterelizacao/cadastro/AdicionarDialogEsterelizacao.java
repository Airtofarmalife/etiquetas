package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.cadastro;

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
public class AdicionarDialogEsterelizacao extends JDialog {

    private final AdicionarPainelFormularioEsterelizacao formPanel;
    private final AdicionarPainelBotoesEsterelizacao formBtnPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.USUARIO_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(250,250));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        customizeCloseOperation();
    }

    private void initComponents() {
        add(formPanel, BorderLayout.CENTER);
        add(formBtnPanel, BorderLayout.SOUTH);
    }

    private void customizeCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                formPanel.clearForm();
            }
        });
    }
}
