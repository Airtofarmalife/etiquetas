package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.login.LoginDTO;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.LoginService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.view.LoginFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.view.LoginFormPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.view.LoginJDialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.controller.MainMenuController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class LoginController extends AbstractFrameController {

    private final LoginJDialog loginJDialog;

    private final LoginService loginService;

    private final MainMenuController mainMenuController;

    @PostConstruct
    private void prepareListeners() {

        LoginFormBtnPanel formBtnPanel = loginJDialog.getLoginFormBtnPanel();
        registerAction(formBtnPanel.getSaveBtn(), (e) -> login());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeLoginJdialog());
    }

    @Override
    public void prepareAndOpenFrame() {

        // Recupera usuários que podem fazer login (Gesto,Administrador e Padrão)
        JComboBox loginCombobox = loginJDialog.getLoginFormPanel().getLoginsComboBox();
        for (Usuario usuario : loginService.getLogins()) {
            loginCombobox.addItem(usuario.getNome());
        }

        loginJDialog.setSize(new Dimension(450,260));
        loginJDialog.setPreferredSize(new Dimension(450,260));
        loginJDialog.setLocationRelativeTo(null);
        loginJDialog.setAlwaysOnTop(false);
        loginJDialog.pack();
        loginJDialog.setVisible(true);
    }

    private void login() {

        LoginFormPanel loginFormPanel = loginJDialog.getLoginFormPanel();
        if (loginFormPanel.validateForm()) {

            LoginDTO loginDTO = loginJDialog.getLoginFormPanel().getEntityFromForm();

            boolean logadoComSucesso = loginService.login(loginDTO.getUsuario(),loginDTO.getSenha());

            if (logadoComSucesso) {
                closeLoginJdialog();
                mainMenuController.getMainMenuFrame().setUpFrame();
                mainMenuController.prepareAndOpenFrame();
            }
        }
    }

    private void closeLoginJdialog() {
        loginJDialog.getLoginFormPanel().clearForm();
        loginJDialog.dispose();
    }

}
