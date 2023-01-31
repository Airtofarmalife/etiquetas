package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.controller.KitController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class KitMenuListener extends JFrame implements ActionListener {

    private KitController kitController;

    public KitMenuListener(KitController kitController)  {
        this.kitController = kitController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        kitController.prepareAndOpenFrame();
    }
}