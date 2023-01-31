package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.controller.EtiquetaController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component

public class EtiquetaListener extends JFrame implements ActionListener {

    private EtiquetaController etiquetaController;

    public EtiquetaListener(EtiquetaController etiquetaController)  {
        this.etiquetaController = etiquetaController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        etiquetaController.prepareAndOpenFrame();
    }
}
