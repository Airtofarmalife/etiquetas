package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.controller.PeriodoController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class PeriodoMenuListener extends JFrame implements ActionListener {

    private PeriodoController periodoController;

    public PeriodoMenuListener(PeriodoController periodoController)  {
        this.periodoController = periodoController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        periodoController.prepareAndOpenFrame();
    }
}