package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.ResponsavelEsterelizacaoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.ResponsavelPreparoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class ResponsavelPreparoMenuListener extends JFrame implements ActionListener {

    private final ResponsavelPreparoController responsavelPreparoController;

    public ResponsavelPreparoMenuListener(ResponsavelPreparoController responsavelPreparoController)  {
        this.responsavelPreparoController = responsavelPreparoController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        responsavelPreparoController.prepareAndOpenFrame(EnumUsuariosType.RESPONSAVEL_PREPARO);
    }
}