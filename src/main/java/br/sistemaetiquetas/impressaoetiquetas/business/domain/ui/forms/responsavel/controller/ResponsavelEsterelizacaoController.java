package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.LoginService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario.UsuarioService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.model.UsuarioTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.cadastro.AdicionarPainelBotoesEsterelizacao;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.listagem.ListarPainelBotoesEsterelizacao;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.listagem.ListarFrameEsterelizacao;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.cadastro.AdicionarDialogEsterelizacao;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.cadastro.AdicionarPainelFormularioEsterelizacao;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.List;

@Controller
@AllArgsConstructor
public class ResponsavelEsterelizacaoController {

    private final ListarFrameEsterelizacao listarFrameEsterelizacao;
    private final AdicionarDialogEsterelizacao adicionarDialogEsterelizacao;
    private final UsuarioTableModel usuarioTableModel;
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @PostConstruct
    private void prepareListeners() {

        ListarPainelBotoesEsterelizacao listarPainelBotoesEsterelizacao = listarFrameEsterelizacao.getTableBtnPanel();
        listarPainelBotoesEsterelizacao.getAddBtn().addActionListener((e) -> exibirJanelaCadastro());
        listarPainelBotoesEsterelizacao.getRemoveBtn().addActionListener((e) -> removeEntity());

        AdicionarPainelBotoesEsterelizacao adicionarPainelBotoesEsterelizacao = adicionarDialogEsterelizacao.getFormBtnPanel();
        adicionarPainelBotoesEsterelizacao.getSaveBtn().addActionListener((e) -> saveEntity());
        adicionarPainelBotoesEsterelizacao.getCancelBtn().addActionListener((e) -> closeModalWindow());
    }

    public void prepareAndOpenFrame(EnumUsuariosType usuariosType) {
        loadEntities(usuariosType);
        exibirJanelaListagem();
    }

    private void loadEntities(EnumUsuariosType enumUsuariosType) {

        List<Usuario> entities = usuarioService.findByUsuariosType(enumUsuariosType);
        usuarioTableModel.clear();
        usuarioTableModel.addEntities(entities);
    }

    private void exibirJanelaListagem() { listarFrameEsterelizacao.setVisible(true); }

    private void exibirJanelaCadastro() { adicionarDialogEsterelizacao.setVisible(true); }

    private void saveEntity() {

        AdicionarPainelFormularioEsterelizacao formPanel = adicionarDialogEsterelizacao.getFormPanel();

        if (formPanel.validateForm()) {
            Usuario usuario = formPanel.getEntityFromForm();

            // Cria hash com nome completo
            String nomeCompleto = usuario.getNome() + usuario.getNomeComplementar().trim();
            String hashedPassword = loginService.hashPassword(nomeCompleto, usuario.getSenha());
            usuario.setSenha(hashedPassword);

            usuarioService.save(usuario);
            usuarioTableModel.addEntity(usuario);
            closeModalWindow();
        }
    }

    private void closeModalWindow() {
        adicionarDialogEsterelizacao.getFormPanel().clearForm();
        adicionarDialogEsterelizacao.dispose();
    }

    private void removeEntity() {
        try {
            JTable clientTable = listarFrameEsterelizacao.getTablePanel().getTable();
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
