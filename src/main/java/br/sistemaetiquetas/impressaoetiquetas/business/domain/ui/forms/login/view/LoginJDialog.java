package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@AllArgsConstructor
@Getter
public class LoginJDialog extends JFrame {

    private final LoginFormPanel loginFormPanel;
    private final LoginFormBtnPanel loginFormBtnPanel;

    @PostConstruct
    private void prepareFrame() {
        setFrameUp();
        initComponents();
        pack();
    }
    private void setFrameUp() {
        setTitle(ConstMessages.DialogTitles.LOGIN);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        add(loginFormPanel, BorderLayout.CENTER);
        add(loginFormBtnPanel, BorderLayout.SOUTH);
    }
}
