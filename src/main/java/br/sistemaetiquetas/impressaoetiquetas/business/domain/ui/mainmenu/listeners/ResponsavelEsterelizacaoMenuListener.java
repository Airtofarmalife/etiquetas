package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.ResponsavelEsterelizacaoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class ResponsavelEsterelizacaoMenuListener extends JFrame implements ActionListener {

    private final ResponsavelEsterelizacaoController responsavelEsterelizacaoController;

    public ResponsavelEsterelizacaoMenuListener(ResponsavelEsterelizacaoController responsavelEsterelizacaoController)  {
        this.responsavelEsterelizacaoController = responsavelEsterelizacaoController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        responsavelEsterelizacaoController.prepareAndOpenFrame(EnumUsuariosType.RESPONSAVEL_ESTERELIZACAO);
    }
}