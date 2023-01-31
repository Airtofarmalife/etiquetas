package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.view.responsavel_esterelizacao.cadastro;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
public class AdicionarPainelFormularioEsterelizacao extends JPanel {

    private JTextField nomeTxtField;
    private JTextField nomeComplementarTxtField;
    private JTextField senhaTxtField;

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informações do usuário"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 10 20,insets 25 20 25 20"));
    }

    private void initComponents() {

        // Nome
        JLabel nomeLbl = new JLabel(ConstMessages.Labels.USUARIO_NOME);
        nomeTxtField = new JTextField(11);
        nomeTxtField.setDocument(new JTextFieldLimit(10));

        // Nome Complementar
        JLabel nomeComplementarLbl = new JLabel(ConstMessages.Labels.USUARIO_NOME_COMPLEMENTAR);
        nomeComplementarTxtField = new JTextField(11);
        nomeComplementarTxtField.setDocument(new JTextFieldLimit(10));

        //Senha
        JLabel senhaLabel = new JLabel(ConstMessages.Labels.USUARIO_LOGIN_SENHA);
        senhaTxtField = new JPasswordField(10);
        senhaTxtField.setDocument(new JTextFieldLimit(10));

        add(nomeLbl,"align label");
        add(nomeTxtField,"split 3");
        add(nomeComplementarLbl,"align label");
        add(nomeComplementarTxtField,"wrap");
        add(senhaLabel,"align label");
        add(senhaTxtField,"wrap");
    }

    public boolean validateForm() {

        if (Strings.isNullOrEmpty(nomeTxtField.getText()) ||
                nomeTxtField.getText().length() > 10) {
            Notifications.showAlertMessage("O nome deve conter entre 1 e 10 caracteres");
            return false;
        }

        if (nomeComplementarTxtField.getText().length() > 10) {
            Notifications.showAlertMessage("O nome complementar não pode ultrapassar 10 caracteres");
            return false;
        }

        if (senhaTxtField.getText().length() < 5 || senhaTxtField.getText().length() > 10) {
            Notifications.showAlertMessage("A senha deve conter entre 5 e 10 caracteres");
            return false;
        }

        return true;
    }

    public Usuario getEntityFromForm() {

        Usuario usuario = new Usuario();
        usuario.setNome(nomeTxtField.getText().trim());
        usuario.setNomeComplementar(nomeComplementarTxtField.getText().trim());
        usuario.setSenha(senhaTxtField.getText().trim());
        usuario.setUsuariosType(EnumUsuariosType.RESPONSAVEL_ESTERELIZACAO);
        return usuario;
    }

    public void clearForm() {

        nomeTxtField.setText("");
        nomeComplementarTxtField.setText("");
        senhaTxtField.setText("");
    }

}
