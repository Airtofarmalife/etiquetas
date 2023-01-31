package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.login.LoginDTO;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
@Getter
public class LoginFormPanel extends JPanel {

    private JComboBox loginsComboBox = new JComboBox();
    private JTextField senhaJPassField;

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Identificação"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 7 7,insets 10 8 10 8"));
    }

    public void initComponents() {

        JLabel usuarioLabel = new JLabel(ConstMessages.Labels.LOGIN_USUARIO);

        loginsComboBox.setMaximumRowCount(3);

        JLabel senhaLabel = new JLabel(ConstMessages.Labels.LOGIN_SENHA);
        senhaJPassField = new JPasswordField(10);
        senhaJPassField.setDocument(new JTextFieldLimit(10));

        add(usuarioLabel,"align label");
        add(loginsComboBox,"wrap");
        add(senhaLabel,"align label");
        add(senhaJPassField,"wrap");

        revalidate();

    }

    public boolean validateForm() {

        if (Strings.isNullOrEmpty(senhaJPassField.getText())) {
            Notifications.showAlertMessage("Senha não informada!");
            return false;
        }

        return true;
    }


    public LoginDTO getEntityFromForm() {

        return LoginDTO.builder()
                .usuario(loginsComboBox.getSelectedItem().toString())
                .senha(senhaJPassField.getText())
                .build();
    }

    public void clearForm() {
        senhaJPassField.setText("");
    }

}
