package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
@Getter
public class ImportacaoFormBtnPanel extends JPanel {

    private JButton enviarBtn;
    private JButton cancelarBtn;

    @PostConstruct
    private void preparePanel() {
        initComponents();
    }

    private void initComponents() {

        enviarBtn = new JButton(ConstMessages.Labels.BOTAO_ENVIAR);
        add(enviarBtn);

        cancelarBtn = new JButton(ConstMessages.Labels.BOTAO_CANCELAR);
        add(cancelarBtn);
    }

}
