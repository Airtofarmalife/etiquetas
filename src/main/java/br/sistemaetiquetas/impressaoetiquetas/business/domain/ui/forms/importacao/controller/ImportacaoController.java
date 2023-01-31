package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.kit.KitService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view.ImportacaoFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view.ImportacaoJDialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import javax.annotation.PostConstruct;
import java.io.File;

@Controller
@AllArgsConstructor
public class ImportacaoController extends AbstractFrameController {

    private final ImportacaoJDialog importacaoJDialog;
    private final KitService kitService;

    @PostConstruct
    private void prepareListeners() {

        ImportacaoFormBtnPanel formBtnPanel = importacaoJDialog.getImportacaoFormBtnPanel();
        registerAction(formBtnPanel.getEnviarBtn(), (e) -> importar());
        registerAction(formBtnPanel.getCancelarBtn(), (e) -> fecharDialog());
    }

    @Override
    public void prepareAndOpenFrame() {

        importacaoJDialog.setAlwaysOnTop(false);
        importacaoJDialog.setVisible(true);
    }

    private void importar() {

        br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.view.ImportacaoFormPanel importacaoFormPanel = importacaoJDialog.getImportacaoFormPanel();
        if (importacaoFormPanel.validateForm()) {
            File arquivoExcel = new File(importacaoFormPanel.getArquivoExcel().getText());
            if(kitService.importar(arquivoExcel)) {
                Notifications.showAlertMessage("Importaçâo realizada com sucesso");
            }
        }
    }


    private void fecharDialog() {

        importacaoJDialog.getImportacaoFormPanel().clearForm();
        importacaoJDialog.dispose();
    }

}
