package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
@Getter
public class HistoricoFormBtnPanel extends JPanel {

    private JButton printBtn;
    private JButton cancelBtn;

    @PostConstruct
    private void preparePanel() {
        initComponents();
    }

    private void initComponents() {

        printBtn = new JButton(ConstMessages.Labels.HISTORICO_BOTAO_IMPRESSAO);
        add(printBtn);

        cancelBtn = new JButton(ConstMessages.Labels.BOTAO_CANCELAR);
        add(cancelBtn);
    }

}
