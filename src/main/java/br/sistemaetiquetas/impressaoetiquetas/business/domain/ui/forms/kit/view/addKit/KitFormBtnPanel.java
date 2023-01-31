package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
@Getter
public class KitFormBtnPanel extends JPanel {

    private JButton saveBtn;
    private JButton cancelBtn;

    @PostConstruct
    private void preparePanel() {
        initComponents();
    }

    private void initComponents() {
        saveBtn = new JButton(ConstMessages.Labels.BOTAO_SALVAR);
        add(saveBtn);

        cancelBtn = new JButton(ConstMessages.Labels.BOTAO_CANCELAR);
        add(cancelBtn);

    }

}
