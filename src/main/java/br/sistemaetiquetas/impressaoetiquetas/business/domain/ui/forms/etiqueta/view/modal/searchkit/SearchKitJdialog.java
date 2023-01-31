package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.searchkit;

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
public class SearchKitJdialog extends JDialog {

    private SearchKitJpanel searchKitJpanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    @Override
    public void setVisible(boolean b) {
        searchKitJpanel.removeAll();
        searchKitJpanel.initPanel();
        searchKitJpanel.revalidate();
        searchKitJpanel.repaint();
        searchKitJpanel.getJtfFilter().setText("");
        pack();
        super.setVisible(b);

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
        add(searchKitJpanel, BorderLayout.CENTER);
    }
}
