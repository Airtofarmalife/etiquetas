package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.search;

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
public class ListKitJdialog extends JDialog {

    private ListKitJpanel listKitJpanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    @Override
    public void setVisible(boolean isVisible) {

        listKitJpanel.removeAll();
        listKitJpanel.initPanel();
        listKitJpanel.revalidate();
        listKitJpanel.repaint();
        listKitJpanel.getJtfFilter().setText("");
        pack();
        super.setVisible(isVisible);
    }

    private void setFrameUp() {
        setTitle(ConstMessages.Labels.ETIQUETAS_PROCURAR_KIT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100,500);
        setPreferredSize(new Dimension(1100,500));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {
        add(listKitJpanel, BorderLayout.CENTER);
    }
}
