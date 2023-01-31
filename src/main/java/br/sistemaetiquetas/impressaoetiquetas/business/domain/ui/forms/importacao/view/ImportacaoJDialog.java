package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view;

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
public class ImportacaoJDialog extends JDialog {

    private final ImportacaoFormPanel importacaoFormPanel;
    private final ImportacaoFormBtnPanel importacaoFormBtnPanel;

    @PostConstruct
    private void prepareFrame() {

        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {

        setTitle(ConstMessages.DialogTitles.IMPORTACAO);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(600,170));
        setPreferredSize(new Dimension(600,170));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {

        add(importacaoFormPanel, BorderLayout.CENTER);
        add(importacaoFormBtnPanel, BorderLayout.SOUTH);
    }
}
