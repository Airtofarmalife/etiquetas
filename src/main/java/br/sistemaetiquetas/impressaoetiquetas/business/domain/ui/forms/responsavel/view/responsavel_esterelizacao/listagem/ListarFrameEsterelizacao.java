package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.listagem;

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
public class ListarFrameEsterelizacao extends JFrame {

    private final ListarPainelTabelaEsterelizacao tablePanel;
    private final ListarPainelBotoesEsterelizacao tableBtnPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.Labels.USUARIOS_ESTERELIZADOR);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(430, 350);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        add(tablePanel, BorderLayout.CENTER);
        add(tableBtnPanel, BorderLayout.SOUTH);
    }

}
