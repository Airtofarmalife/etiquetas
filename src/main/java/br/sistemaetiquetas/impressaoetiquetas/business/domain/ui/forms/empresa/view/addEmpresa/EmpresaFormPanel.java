package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.addEmpresa;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Getter
public class EmpresaFormPanel extends JPanel {

    private JTextField nomeTextField;
    private JTextField validadeTextField;

    private Empresa empresa = null;

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informações da empresa"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 7 7,insets 10 8 10 8"));

    }

    public void initComponents() {
        removeAll();
        JLabel nomeLbl = new JLabel(ConstMessages.Labels.EMPRESA_NOME);
        nomeTextField = new JTextField(30);
        nomeTextField.setDocument(new JTextFieldLimit(28));

        JLabel validadeLbl = new JLabel(ConstMessages.Labels.EMPRESA_VALIDADE);

        LocalDate dataValidate = LocalDate.now().plusMonths(4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        String textoValidade = dataValidate.format(formatter);

        validadeTextField = new JTextField(9);
        validadeTextField.setDocument(new JTextFieldLimit(10));
        validadeTextField.setText(textoValidade);

        add(nomeLbl,"align label");
        add(nomeTextField,"wrap");
        add(validadeLbl,"align label");
        add(validadeTextField,"wrap");

        revalidate();
    }

    public boolean validateForm() {

        // Nome
        if (Strings.isNullOrEmpty(nomeTextField.getText()) ||
                nomeTextField.getText().length() > 28) {
            Notifications.showAlertMessage("O nome da empresa deve conter entre 1 e 30 caracteres");
            return false;
        }

        if (Strings.isNullOrEmpty(validadeTextField.getText().trim())) {
            Notifications.showAlertMessage("Data de validade não informada");
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(validadeTextField.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (LocalDate.now().isAfter(date)) {
                Notifications.showAlertMessage("A data de validade não pode ser no passado");
                return false;
            }
        } catch (DateTimeParseException exception) {
            Notifications.showAlertMessage("Formato de data incorreto");
            return false;
        }
        return true;
    }

    public Empresa getEntityFromForm() {

        if (this.empresa == null) {
            this.empresa = new Empresa();
        }

        this.empresa.setNome(nomeTextField.getText());
        LocalDate dataValidade = LocalDate.parse(validadeTextField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.empresa.setValidade(dataValidade);
        return this.empresa;
    }

    public void setEntityOnForm(Empresa empresa) {
        this.empresa = empresa;
        nomeTextField.setText(this.empresa.getNome());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String textoValidade = this.empresa.getValidade().format(formatter);
        validadeTextField.setText(textoValidade);
    }

    public void clearForm() {
        this.empresa = null;
        nomeTextField.setText("");
        validadeTextField.setText("");
    }

}
