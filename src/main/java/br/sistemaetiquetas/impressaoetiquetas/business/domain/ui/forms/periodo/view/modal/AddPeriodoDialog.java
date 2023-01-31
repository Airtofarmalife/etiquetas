package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.modal;

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
public class AddPeriodoDialog extends JDialog {

    private final PeriodoFormPanel formPanel;
    private final PeriodoFormBtnPanel formBtnPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.PERIODO_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300,250);
        setPreferredSize(new Dimension(300,250));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {
        add(formPanel, BorderLayout.CENTER);
        add(formBtnPanel, BorderLayout.SOUTH);
    }
}
