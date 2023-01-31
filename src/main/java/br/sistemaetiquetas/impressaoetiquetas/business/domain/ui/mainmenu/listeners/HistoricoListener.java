package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.controller.HistoricoController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component

public class HistoricoListener extends JFrame implements ActionListener {

    private HistoricoController historicoController;

    public HistoricoListener(HistoricoController historicoController)  {
        this.historicoController = historicoController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        historicoController.prepareAndOpenFrame();
    }
}
