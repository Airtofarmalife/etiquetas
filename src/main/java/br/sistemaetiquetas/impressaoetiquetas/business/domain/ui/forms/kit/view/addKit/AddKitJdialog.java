package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit;

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
public class AddKitJdialog extends JDialog {

    private final KitFormPanel formPanel;
    private final KitFormBtnPanel formBtnPanel;
    private final KitAnexoPanel kitAnexoPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.KIT_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {
        add(kitAnexoPanel,BorderLayout.WEST);
        add(formPanel, BorderLayout.CENTER);
        add(formBtnPanel, BorderLayout.SOUTH);
        kitAnexoPanel.setVisible(false);
    }
}
