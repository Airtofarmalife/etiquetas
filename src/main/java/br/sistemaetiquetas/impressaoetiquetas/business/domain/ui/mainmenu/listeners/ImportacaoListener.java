package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.controller.HistoricoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.controller.ImportacaoController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component

public class ImportacaoListener extends JFrame implements ActionListener {

    private ImportacaoController importacaoController;

    public ImportacaoListener(ImportacaoController importacaoController)  {
        this.importacaoController = importacaoController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        importacaoController.prepareAndOpenFrame();
    }
}
