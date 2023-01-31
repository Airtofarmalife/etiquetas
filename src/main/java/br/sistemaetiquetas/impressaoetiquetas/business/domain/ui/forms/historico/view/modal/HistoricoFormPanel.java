package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa.EmpresaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.etiqueta.EtiquetaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.periodo.PeriodoService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.Util;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
public class HistoricoFormPanel extends JPanel {

    private JLabel tipoRelatorio = new JLabel(ConstMessages.Labels.HISTORICO_RELATORIO);
    private JLabel nomePeriodoLbl = new JLabel(ConstMessages.Labels.PERIODO_NOME);
    private JLabel empresaLbl = new JLabel(ConstMessages.Labels.HISTORICO_EMPRESAS);
    private JLabel dataInicialLbl = new JLabel(ConstMessages.Labels.HISTORICO_PERIODO_INICIAL);
    private JLabel dataFinalLbl = new JLabel(ConstMessages.Labels.HISTORICO_PERIODO_FINAL);
    private JLabel tipoLbl = new JLabel(ConstMessages.Labels.HISTORICO_TIPO);
    private JLabel envolucrosLbl = new JLabel(ConstMessages.Labels.ETIQUETAS_ENVOLUCRO_LABEL);
    private JLabel relatorioCanceladoLbl = new JLabel(ConstMessages.Labels.HISTORICO_RELATORIO_CANCELADO);
    private JLabel envolucrosSelecionadosLbl = new JLabel(" ");
    private JLabel tiposEnvolucroLbl = new JLabel(ConstMessages.Labels.HISTORICO_TIPO_ENVOLUCRO);
    private JComboBox empresaComboBox = new JComboBox();
    private JComboBox periodoComboBox = new JComboBox();
    private JComboBox tipoComboBox = new JComboBox();
    private JComboBox tiposEnvolucroCombobox = new JComboBox();
    private JTextField horaInicialTextField = new JTextField(4);
    private JTextField horaFinalTextField = new JTextField(4);
    private JDateChooser diaInicialChooser = new JDateChooser("dd/MM/yyyy","##/##/####", '_');
    private JDateChooser diaFinalChooser = new JDateChooser("dd/MM/yyyy","##/##/####", '_');
    private JButton envolucrosButton = new JButton("Selecionar");
    private CheckboxGroup relatoriosGroupCheckbox = new CheckboxGroup();
    private Checkbox turnosCheckbox = new Checkbox("Turnos", relatoriosGroupCheckbox, false);
    private Checkbox envolucrosCheckbox = new Checkbox("Envólucros", relatoriosGroupCheckbox, false);
    private Checkbox livreCheckbox = new Checkbox("Livre", relatoriosGroupCheckbox, false);
    private Checkbox relatorioCanceladoCheckbox = new Checkbox("ABORTADO",false);
    private JTextArea envolucroTextArea = new JTextArea(12,10);
    private JScrollPane envolucroScrollPane = new JScrollPane(envolucroTextArea);

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private PeriodoService periodoService;
    @Setter
    private ArrayList<String> envolucros;
    @Autowired
    private EtiquetaService etiquetaService;

    @PostConstruct
    private void preparePanel() {

        loadPeriodos();
        loadEmpresas();
        setPanelUp();
        initComponents();
    }

    private void setPanelUp() {

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Parâmetros do relatório"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 10 10,insets 12 9 12 9, hidemode 1"));
    }

    private void initComponents() {

        // Envolucros
        envolucros = new ArrayList<>();

        // Empresas
        empresaComboBox.setMaximumRowCount(empresaComboBox.getModel().getSize()+1);

        // Tipos
        tipoComboBox.addItem("");
        tipoComboBox.addItem("Autoclave");
        tipoComboBox.addItem("Peróxido de Hidrogênio");
        tipoComboBox.addItem("Desinfecção de Alto Nível");
        tipoComboBox.setLightWeightPopupEnabled(false);

        // Checkbox relatório -> turnos
        turnosCheckbox.addItemListener(e -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    habilitarCamposRelatorioEnvolucro(false);
                    habilitarCamposRelatorioHorarioLivre(false);
                    habilitarCamposRelatorioTurnos(true);
                    break;
            }
        });

        // Checkbox relatório -> envolucros
        envolucrosCheckbox.addItemListener(e -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    habilitarCamposRelatorioTurnos(false);
                    habilitarCamposRelatorioHorarioLivre(false);
                    habilitarCamposRelatorioEnvolucro(true);
                    break;
            }
        });

        // Checkbox relatório -> horario livre
        livreCheckbox.addItemListener(e -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    habilitarCamposRelatorioTurnos(false);
                    habilitarCamposRelatorioEnvolucro(false);
                    habilitarCamposRelatorioHorarioLivre(true);
                    break;

            }
        });

        // Dia inicial
        Date caledarDateObj = Util.convertLocalDateToDate(LocalDate.now());
        diaInicialChooser.setDate(caledarDateObj);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) diaInicialChooser.getDateEditor();
        editor.setEditable(false);
        editor.setDisabledTextColor(Color.BLACK);

        // Dia final
        diaFinalChooser.setDate(caledarDateObj);
        editor = (JTextFieldDateEditor) diaFinalChooser.getDateEditor();
        editor.setEditable(false);
        editor.setDisabledTextColor(Color.BLACK);

        // Turnos
        periodoComboBox.setMaximumRowCount(periodoComboBox.getModel().getSize()+1);
        periodoComboBox.setEditable(false);
        periodoComboBox.addItemListener(e -> {

            if (e.getID() == ItemEvent.ITEM_STATE_CHANGED && turnosCheckbox.getState()) {

                if (e.getStateChange() == ItemEvent.SELECTED && !Strings.isNullOrEmpty(e.getItem().toString().trim())) {

                    if(e.getItem().toString().equals("Dia")) {
                        horaInicialTextField.setVisible(false);
                        horaFinalTextField.setVisible(false);

                    } else {

                        horaInicialTextField.setVisible(true);
                        horaFinalTextField.setVisible(true);
                        loadHorarios(e.getItem().toString());

                        // Verifica se é turno extendido
                        LocalTime horaInicial = LocalTime.parse(horaInicialTextField.getText());
                        LocalTime horaFinal = LocalTime.parse(horaFinalTextField.getText());
                        LocalDate localDateInicial = Util.convertDateToLocalDate(diaInicialChooser.getDate());
                        if (horaFinal.isBefore(horaInicial)) {
                            diaFinalChooser.setDate(Util.convertLocalDateToDate(localDateInicial.plusDays(1)));
                        } else {
                            diaFinalChooser.setDate(Util.convertLocalDateToDate(localDateInicial));
                        }
                    }
                    revalidate();
                    repaint();

                } else {

                    LocalTime horarioInicial = LocalTime.of(00, 00, 00, 0);
                    horaInicialTextField.setText(horarioInicial.toString());
                    horaInicialTextField.setEditable(true);
                    horaInicialTextField.setBackground(Color.WHITE);

                    LocalTime horarioFinal = LocalTime.of(23, 59, 00, 0);
                    horaFinalTextField.setText(horarioFinal.toString());
                    horaFinalTextField.setEditable(true);
                    horaFinalTextField.setBackground(Color.WHITE);
                }
            }
        });

        // Horarios
        LocalTime horarioInicial = LocalTime.of(00, 00, 00, 0);
        LocalTime horarioFinal = LocalTime.of(23, 59, 00, 0);
        horaInicialTextField.setText(horarioInicial.toString());
        horaInicialTextField.setDocument(new JTextFieldLimit(5));
        horaFinalTextField.setText(horarioFinal.toString());
        horaFinalTextField.setDocument(new JTextFieldLimit(5));

        // Tipos de envolucro
        tiposEnvolucroCombobox.addItem("");
        tiposEnvolucroCombobox.addItem("PGRAU");
        tiposEnvolucroCombobox.addItem("SMS");
        tiposEnvolucroCombobox.setEditable(false);
        tiposEnvolucroCombobox.addItemListener(e -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                   loadEnvolucros();
                    break;
            }
        });

        // Envolucros text area
        envolucroTextArea.setEditable(false);
        envolucroTextArea.setLineWrap(true);
        envolucroTextArea.setWrapStyleWord(true);
        envolucroTextArea.setMargin(new Insets(2,2,2,2));

        add(empresaLbl,"align label");
        add(empresaComboBox, "span");
        add(tipoLbl,"align label");
        add(tipoComboBox,"wrap");
        add(tipoRelatorio,"align label");
        add(turnosCheckbox,"split 3");
        add(envolucrosCheckbox);
        add(livreCheckbox,"wrap");
        add(tiposEnvolucroLbl,"align label");
        add(tiposEnvolucroCombobox,"wrap");
        add(envolucrosLbl,"align label");
        add(envolucrosButton,"wrap");
        add(envolucrosSelecionadosLbl,"align label");
        add(envolucroScrollPane,"wrap");
        add(nomePeriodoLbl,"align label");
        add(periodoComboBox,"wrap");
        add(dataInicialLbl,"align label");
        add(diaInicialChooser,"split 2");
        add(horaInicialTextField,"wrap");
        add(dataFinalLbl,"align label");
        add(diaFinalChooser,"split 2");
        add(horaFinalTextField,"wrap");
        add(relatorioCanceladoLbl,"align label");
        add(relatorioCanceladoCheckbox);

        habilitarCamposRelatorioTurnos(false);
        habilitarCamposRelatorioEnvolucro(false);
        habilitarCamposRelatorioHorarioLivre(false);

        revalidate();
        repaint();
    }

    public void habilitarCamposRelatorioTurnos(boolean habilitado) {

        nomePeriodoLbl.setVisible(habilitado);
        periodoComboBox.setVisible(habilitado);
        dataInicialLbl.setVisible(habilitado);
        diaInicialChooser.setVisible(habilitado);
        horaInicialTextField.setVisible(habilitado);
        horaInicialTextField.setEditable(false);
        dataFinalLbl.setVisible(habilitado);
        diaFinalChooser.setVisible(habilitado);
        horaFinalTextField.setVisible(habilitado);
        horaFinalTextField.setEditable(false);
        periodoComboBox.setSelectedItem("Manhã");
        loadHorarios(periodoComboBox.getSelectedItem().toString());
    }

    public void habilitarCamposRelatorioEnvolucro(boolean habilitado) {

        envolucrosLbl.setVisible(habilitado);
        envolucrosButton.setVisible(habilitado);
        dataInicialLbl.setVisible(habilitado);
        diaInicialChooser.setVisible(habilitado);
        dataFinalLbl.setVisible(habilitado);
        diaFinalChooser.setVisible(habilitado);
        tiposEnvolucroLbl.setVisible(habilitado);
        tiposEnvolucroCombobox.setVisible(habilitado);
        envolucrosSelecionadosLbl.setVisible(habilitado);
        envolucroTextArea.setVisible(habilitado);
        envolucroTextArea.setText("");
        envolucroScrollPane.setVisible(habilitado);
        loadEnvolucros();
    }

    public void habilitarCamposRelatorioHorarioLivre(boolean habilitado) {

        dataInicialLbl.setVisible(habilitado);
        diaInicialChooser.setVisible(habilitado);
        dataFinalLbl.setVisible(habilitado);
        diaFinalChooser.setVisible(habilitado);

        LocalTime horarioInicial = LocalTime.of(00, 00, 00, 0);
        horaInicialTextField.setText(horarioInicial.toString());
        horaInicialTextField.setVisible(habilitado);
        horaInicialTextField.setEditable(true);
        horaInicialTextField.setBackground(Color.WHITE);

        LocalTime horarioFinal = LocalTime.of(23, 59, 00, 0);
        horaFinalTextField.setText(horarioFinal.toString());
        horaFinalTextField.setVisible(habilitado);
        horaFinalTextField.setEditable(true);
        horaFinalTextField.setBackground(Color.WHITE);

        relatorioCanceladoLbl.setVisible(habilitado);
        relatorioCanceladoCheckbox.setVisible(habilitado);
        relatorioCanceladoCheckbox.setState(false);
    }

    public void loadEmpresas() {

        List<String> empresas = empresaService.findAll().stream().map(Empresa::getNome).collect(Collectors.toList());
        empresaComboBox.removeAllItems();
        empresaComboBox.addItem("");
        for (String empresa: empresas) {
            empresaComboBox.addItem(empresa);
        }

        empresaComboBox.setMaximumRowCount(empresaComboBox.getModel().getSize());
    }

    public void loadPeriodos() {

        List<String> periodos = periodoService.findAll().stream().map(Periodo::getNome).collect(Collectors.toList());
        periodoComboBox.removeAllItems();
        for (String periodo: periodos) {
            periodoComboBox.addItem(periodo);
        }
        periodoComboBox.addItem("Dia");
    }

    private void loadHorarios(String nomeHorario) {

        horaInicialTextField.setText("");
        horaFinalTextField.setText("");

        if (!periodoComboBox.getSelectedItem().equals("Dia")) {
            Periodo periodo = periodoService.findByNome(nomeHorario);

            horaInicialTextField.setText(periodo.getHoraInicial().toString());
            horaInicialTextField.setEditable(false);
            horaInicialTextField.setBackground(Color.LIGHT_GRAY);
            horaFinalTextField.setVisible(true);

            horaFinalTextField.setText(periodo.getHoraFinal().toString());
            horaFinalTextField.setEditable(false);
            horaFinalTextField.setBackground(Color.LIGHT_GRAY);
            horaFinalTextField.setVisible(true);
        }
    }

    public void loadEnvolucros() {
        String envolucroSelecionado = tiposEnvolucroCombobox.getSelectedItem().toString().trim();
        List<String> listaEnvolucros;
        if (envolucroSelecionado.length() == 0) {
            listaEnvolucros = etiquetaService.findDistinctByEnvolucro();
        } else {
            listaEnvolucros = etiquetaService.findDistinctByEnvolucroSelecionado(envolucroSelecionado);
        }
        envolucroTextArea.setText("");
        envolucros.clear();
        listaEnvolucros.removeAll(Collections.singleton(""));
        listaEnvolucros.forEach(v -> envolucroTextArea.append(v + "\n"));
        listaEnvolucros.forEach(v -> envolucros.add(v));
    }

    public void clearForm() {

        // Reinicia horário
        LocalTime horarioInicial = LocalTime.of(00, 00, 00, 0);
        LocalTime horarioFinal = LocalTime.of(23, 59, 00, 0);
        horaInicialTextField.setText(horarioInicial.toString());
        horaFinalTextField.setText(horarioFinal.toString());

        // Desmarca tipo de relatório
        relatoriosGroupCheckbox.setSelectedCheckbox(null);
        habilitarCamposRelatorioEnvolucro(false);
        habilitarCamposRelatorioTurnos(false);
        habilitarCamposRelatorioHorarioLivre(false);

        tiposEnvolucroCombobox.setSelectedItem("");

    }

    public boolean validateForm() {

        // Horário inicial
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

        // Horário final
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

        // Valida se data inicial > data final
        LocalDate diaInicio = Util.convertDateToLocalDate(diaInicialChooser.getDate());
        LocalDate diafim = Util.convertDateToLocalDate(diaFinalChooser.getDate());
        // Verifica se o dia inicial é maior que o dia final
        if (diaInicio.isAfter(diafim)) {
            Notifications.showAlertMessage("O dia inicial não pode ser posterior ao dia final");
            return false;
        }

        // horario livre
        if (livreCheckbox.getState()) {
            if (diaInicio.equals(diafim)) {
                LocalTime horaInicio = LocalTime.parse(horaInicialTextField.getText());
                LocalTime horafim = LocalTime.parse(horaFinalTextField.getText());
                if (horaInicio.isAfter(horafim)) {
                    Notifications.showAlertMessage("Horário livre inválido");
                    return false;
                }
            }
        }

        return true;
    }

}
