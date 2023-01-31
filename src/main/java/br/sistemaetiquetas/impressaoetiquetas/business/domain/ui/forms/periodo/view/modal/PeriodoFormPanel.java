package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.modal;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa.EmpresaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import com.google.common.base.Strings;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Component
@Getter
public class PeriodoFormPanel extends JPanel {

    private JTextField nomeTextField = new JTextField(15);
    private JTextField horaInicialTextField = new JTextField(4);
    private JTextField horaFinalTextField = new JTextField(4);
    private JLabel nomeLbl = new JLabel(ConstMessages.Labels.PERIODO_NOME);
    private JLabel horaInicialLbl = new JLabel(ConstMessages.Labels.PERIODO_HORA_INICIAL);
    private JLabel horaFinalLbl = new JLabel(ConstMessages.Labels.PERIODO_HORA_FINAL);

    @Autowired
    private EmpresaService empresaService;

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informações do período"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 10 10,insets 12 9 12 9, hidemode 3"));
    }

    private void initComponents(){

        add(nomeLbl,"align label");
        add(nomeTextField,"wrap");

        add(horaInicialLbl,"align label");
        LocalTime horaInicial = LocalTime.of(00, 00, 00, 0);
        horaInicialTextField.setText(horaInicial.toString());
        add(horaInicialTextField,"wrap");

        add(horaFinalLbl,"align label");
        LocalTime horaFinal = LocalTime.of(23, 59, 00, 0);
        horaFinalTextField.setText(horaFinal.toString());
        add(horaFinalTextField,"wrap");

        this.revalidate();
        this.repaint();
    }


    public void clearForm() {

        nomeTextField.setText("");

        LocalTime horaInicial = LocalTime.of(00, 00, 00, 0);
        horaInicialTextField.setText(horaInicial.toString());

        LocalTime horaFinal = LocalTime.of(23, 59, 00, 0);
        horaFinalTextField.setText(horaFinal.toString());
    }

    public boolean validateForm() {

        if(Strings.isNullOrEmpty(nomeTextField.getText().trim())){
            Notifications.showAlertMessage("Informe o nome do período");
            return false;
        }

        if (Strings.isNullOrEmpty(horaInicialTextField.getText().trim())) {
            Notifications.showAlertMessage("Horário inicial não informado");
            return false;
        }
        try {
            LocalTime.parse(horaInicialTextField.getText());
        } catch (DateTimeParseException exception) {
            Notifications.showAlertMessage("Horário inicial inválido");
            return false;
        }

        if (Strings.isNullOrEmpty(horaFinalTextField.getText().trim())) {
            Notifications.showAlertMessage("Horário final não informado");
            return false;
        }
        try {
            LocalTime.parse(horaFinalTextField.getText());
        } catch (DateTimeParseException exception) {
            Notifications.showAlertMessage("Horário final inválido");
            return false;
        }


        return true;
    }

    public Periodo getEntityFromForm() {

        Periodo periodo = new Periodo();
        periodo.setNome(nomeTextField.getText());
        periodo.setHoraInicial(LocalTime.parse(horaInicialTextField.getText()));
        periodo.setHoraFinal(LocalTime.parse(horaFinalTextField.getText()));
        return periodo;
    }

}