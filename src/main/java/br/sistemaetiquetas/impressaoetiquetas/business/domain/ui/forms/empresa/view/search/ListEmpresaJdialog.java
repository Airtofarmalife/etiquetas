package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.search;

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
public class ListEmpresaJdialog extends JDialog {

    private ListEmpresaJpanel listEmpresaJpanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }

    @Override
    public void setVisible(boolean b) {
        listEmpresaJpanel.removeAll();
        listEmpresaJpanel.initPanel();
        listEmpresaJpanel.revalidate();
        listEmpresaJpanel.repaint();
        pack();
        super.setVisible(b);
    }

    private void setFrameUp() {
        setTitle(ConstMessages.Labels.EMPRESA_BUSCAR);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(480,300);
        setPreferredSize(new Dimension(480,300));
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
    }

    private void initComponents() {
        add(listEmpresaJpanel, BorderLayout.CENTER);
    }
}
