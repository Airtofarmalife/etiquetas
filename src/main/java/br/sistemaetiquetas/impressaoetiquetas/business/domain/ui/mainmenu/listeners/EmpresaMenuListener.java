package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.controller.EmpresaController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class EmpresaMenuListener extends JFrame implements ActionListener {

    private EmpresaController empresaController;

    public EmpresaMenuListener(EmpresaController empresaController)  {
        this.empresaController = empresaController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        empresaController.prepareAndOpenFrame();
    }
}