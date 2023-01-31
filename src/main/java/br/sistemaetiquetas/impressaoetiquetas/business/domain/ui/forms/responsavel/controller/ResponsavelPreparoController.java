package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.LoginService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario.UsuarioService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.model.UsuarioTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_preparo.cadastro.AdicionarDialogPreparo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_preparo.cadastro.AdicionarPainelBotoesPreparo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_preparo.cadastro.AdicionarPainelFormularioPreparo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_preparo.listagem.ListarFramePreparo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_preparo.listagem.ListarPainelBotoesPreparo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.List;

@Controller
@AllArgsConstructor
public class ResponsavelPreparoController {

    private final ListarFramePreparo listarFramePreparo;
    private final AdicionarDialogPreparo adicionarDialogPreparo;
    private final UsuarioTableModel usuarioTableModel;
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @PostConstruct
    private void prepareListeners() {

        ListarPainelBotoesPreparo listarPainelBotoesPreparo = listarFramePreparo.getTableBtnPanel();
        listarPainelBotoesPreparo.getAddBtn().addActionListener((e) -> showAddModal());
        listarPainelBotoesPreparo.getRemoveBtn().addActionListener((e) -> removeEntity());

        AdicionarPainelBotoesPreparo adicionarPainelBotoesPreparo = adicionarDialogPreparo.getFormBtnPanel();
        adicionarPainelBotoesPreparo.getSaveBtn().addActionListener((e) -> saveEntity());
        adicionarPainelBotoesPreparo.getCancelBtn().addActionListener((e) -> closeModalWindow());
    }

    public void prepareAndOpenFrame(EnumUsuariosType usuariosType) {

        loadEntities(usuariosType);
        showTableFrame();
    }

    private void loadEntities(EnumUsuariosType enumUsuariosType) {

        List<Usuario> entities = usuarioService.findByUsuariosType(enumUsuariosType);
        usuarioTableModel.clear();
        usuarioTableModel.addEntities(entities);
    }

    private void showTableFrame() { listarFramePreparo.setVisible(true); }

    private void showAddModal() { adicionarDialogPreparo.setVisible(true); }

    private void saveEntity() {

        AdicionarPainelFormularioPreparo formPanel = adicionarDialogPreparo.getFormPanel();

        adicionarDialogPreparo.setVisible(true);

        if (formPanel.validateForm()) {
            Usuario usuario = formPanel.getEntityFromForm();
            usuarioService.save(usuario);
            usuarioTableModel.addEntity(usuario);
            closeModalWindow();
        }
    }

    private void closeModalWindow() {
        adicionarDialogPreparo.getFormPanel().clearForm();
        adicionarDialogPreparo.dispose();
    }

    private void removeEntity() {
        try {
            JTable clientTable = listarFramePreparo.getTablePanel().getTable();
            int selectedRow = clientTable.getSelectedRow();
            if (selectedRow < 0) {
                Notifications.showNonRowSelected();
            } else {

                Object[] options = { "Sim", "Não" };
                int confirm = JOptionPane.showOptionDialog(
                        null,
                        "Deseja excluir o usuário selecionado?",
                        "Excluir",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (confirm == JOptionPane.YES_OPTION) {

                    Usuario entity = usuarioTableModel.getEntityByRow(selectedRow);
                    usuarioService.remove(entity);
                    usuarioTableModel.removeRow(selectedRow);
                }
            }
        } catch (Exception e) {
            Notifications.showDeleteRowErrorMessage();
        }
    }

}
