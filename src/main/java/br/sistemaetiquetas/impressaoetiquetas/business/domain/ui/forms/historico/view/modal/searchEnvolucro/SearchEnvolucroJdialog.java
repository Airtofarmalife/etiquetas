package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.searchEnvolucro;

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
public class SearchEnvolucroJdialog extends JDialog {

    private SearchEnvolucroJpanel searchEnvolucroJpanel;

    public void setEnvolucroTipo(String tipo) {
        searchEnvolucroJpanel.setTipoEnvolucro(tipo);
    }

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    @Override
    public void setVisible(boolean b) {
        searchEnvolucroJpanel.removeAll();
        searchEnvolucroJpanel.initPanel();
        searchEnvolucroJpanel.revalidate();
        searchEnvolucroJpanel.repaint();
        pack();
        super.setVisible(b);
    }

    private void setFrameUp() {
        setTitle(ConstMessages.Labels.ETIQUETAS_ENVOLUCRO_LABEL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(200, 250);
        setPreferredSize(new Dimension(200,250));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {
        add(searchEnvolucroJpanel, BorderLayout.CENTER);
    }
}
