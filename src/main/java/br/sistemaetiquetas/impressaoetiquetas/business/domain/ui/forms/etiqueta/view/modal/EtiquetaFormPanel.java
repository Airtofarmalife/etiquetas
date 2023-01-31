package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa.EmpresaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario.UsuarioService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
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
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
public class EtiquetaFormPanel extends JPanel {

    private JComboBox empresaComboBox = new JComboBox();
    private JComboBox tipoComboBox = new JComboBox();
    private JComboBox loteComboBox = new JComboBox();
    private JComboBox temperaturaComboBox = new JComboBox();
    private JComboBox temperaturaTipoComboBox = new JComboBox();
    private JComboBox temperaturaValorComboBox = new JComboBox();
    private JComboBox responsavelPreparoComboBox = new JComboBox();
    private JComboBox responsavelEsterelizacaoComboBox = new JComboBox();
    private JComboBox outrasComboBox = new JComboBox();

    private JTextField idKit = new JTextField(5);
    private JTextField codBarras = new JTextField(7);
    private JTextField nomeKit = new JTextField(20);
    private JTextField quantidade = new JTextField(3);
    private JTextField ciclo = new JTextField(8);
    private JTextField observacao = new JTextField(25);

    private JTextField senhaTxtField =  senhaTxtField = new JPasswordField(10);

    private JDateChooser dataEsterelizacao = new JDateChooser("dd/MM/yyyy","##/##/####", '_');
    private JDateChooser dataValidade = new JDateChooser("dd/MM/yyyy","##/##/####", '_');

    private JLabel empresaLbl = new JLabel(ConstMessages.Labels.ETIQUETA_EMPRESA);
    private JLabel consultaLml = new JLabel(ConstMessages.Labels.ETIQUETA_CONSULTA);
    private JLabel codBarrasLbl = new JLabel(ConstMessages.Labels.ETIQUETA_CODBARRAS);
    private JLabel nomeKitLbl = new JLabel(ConstMessages.Labels.ETIQUETA_NOME_KIT);
    private JLabel tipoLbl = new JLabel(ConstMessages.Labels.ETIQUETA_TIPO);
    private JLabel loteLbl = new JLabel(ConstMessages.Labels.ETIQUETA_LOTE);
    private JLabel cicloLbl = new JLabel(ConstMessages.Labels.ETIQUETA_CICLO);
    private JLabel esterelizacaoLbl = new JLabel(ConstMessages.Labels.ETIQUETA_DATA_ESTERELIZACAO);
    private JLabel validadeLbl = new JLabel(ConstMessages.Labels.ETIQUETA_DATA_VALIDADE);
    private JLabel temperaturaLbl = new JLabel(ConstMessages.Labels.ETIQUETA_TEMPERATURA);
    private JLabel observacaoLbl = new JLabel(ConstMessages.Labels.ETIQUETA_OBSERVACAO);
    private JLabel responsavelEsterelizacaoLbl = new JLabel(ConstMessages.Labels.ETIQUETA_RESPONSAVEL_ESTERELIZACAO);
    private JLabel responsavelPreparoLbl = new JLabel(ConstMessages.Labels.ETIQUETA_RESPONSAVEL_PREPARO);
    private JLabel quantidadeLbl = new JLabel(ConstMessages.Labels.ETIQUETA_QUANTIDADE);
    private JLabel outrasLbl = new JLabel(ConstMessages.Labels.ETIQUETA_OUTRAS);
    private JLabel senhaLbl = new JLabel(ConstMessages.Labels.ETIQUETAS_SENHA_RESPONSAVEL);

    private JButton procurarKitBtn = new JButton("...");


    @NotBlank
    @Getter
    @Setter
    private Integer totalEnvolucro;

    @Getter
    @Setter
    @NotBlank
    private String envolucro;

    @Getter
    @Setter
    @NotBlank
    private String tipoEnvolucro;

    @Getter
    @Setter
    @NotBlank
    private String anexo;

    @Getter
    @Setter
    @NotBlank
    private String tamanhoDoKit;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostConstruct
    private void preparePanel() {
        clearForm();
        loadEmpresas();
        loadResponsaveis();
        setPanelUp();
        initComponents();
    }

    public void loadEmpresas(){

        empresaComboBox.removeAllItems();

        List<String> empresas = empresaService.findAll().stream().map(Empresa::getNome).collect(Collectors.toList());
        for (String empresa: empresas) {
            empresaComboBox.addItem(empresa);
        }

        empresaComboBox.setMaximumRowCount(empresaComboBox.getModel().getSize());
    }

    public void loadResponsaveis() {

        responsavelPreparoComboBox.removeAllItems();
        responsavelEsterelizacaoComboBox.removeAllItems();

        List<Usuario> esterelizadores = usuarioService.findByUsuariosType(EnumUsuariosType.RESPONSAVEL_ESTERELIZACAO);
        List<Usuario> preparadores = usuarioService.findByUsuariosType(EnumUsuariosType.RESPONSAVEL_PREPARO);

        List<String> nomesEsterelizadores = new ArrayList<>();
        List<String> nomesPreparadoresEesterelizadores = new ArrayList<>();

        esterelizadores.forEach( (e) -> {
            String nome;
            if (e.getNomeComplementar().length() > 0 ) {
                nome = e.getNome() + "/" + e.getNomeComplementar();
            } else {
                nome = e.getNome();
            }
            nomesEsterelizadores.add(nome);
            nomesPreparadoresEesterelizadores.add(nome);
        });

        preparadores.forEach( (e) -> {
            String nome;
            if (e.getNomeComplementar().length() > 0 ) {
                nome = e.getNome() + "/" + e.getNomeComplementar();
            } else {
                nome = e.getNome();
            }
            nomesPreparadoresEesterelizadores.add(nome);
        });

        Collections.sort(nomesEsterelizadores);
        Collections.sort(nomesPreparadoresEesterelizadores);

        Set<String> preparadoresFinal = new LinkedHashSet<>(nomesPreparadoresEesterelizadores);
        Set<String> esterelizadoresFinal = new LinkedHashSet<>(nomesEsterelizadores);

        preparadoresFinal.forEach(e-> responsavelPreparoComboBox.addItem(e));
        esterelizadoresFinal.forEach(e-> responsavelEsterelizacaoComboBox.addItem(e));

        responsavelPreparoComboBox.setMaximumRowCount(preparadoresFinal.size());
        responsavelEsterelizacaoComboBox.setMaximumRowCount(esterelizadoresFinal.size());
    }

    private void alternarLinhaTemperatura(boolean enable,String item) {

        temperaturaLbl.setVisible(enable);
        temperaturaComboBox.setVisible(enable);
        temperaturaTipoComboBox.setVisible(enable);
        temperaturaValorComboBox.setVisible(enable);
        outrasLbl.setVisible(!enable);
        outrasComboBox.removeAllItems();
        outrasComboBox.addItem(item);
        outrasComboBox.setVisible(!enable);
        this.revalidate();
        this.repaint();
    }

    private void setPanelUp() {

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informações para a etiqueta"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 10 10,insets 12 10 12 10, hidemode 3"));
    }

    private void initComponents(){

        // Empresa
        add(empresaLbl,"align label");

        add(empresaComboBox, "span");

        // Consulta
        add(consultaLml,"align label");
        idKit.setEditable(false);
        idKit.setBackground(Color.LIGHT_GRAY);
        add(idKit,"split 4");

        // Buscar kit
        add(procurarKitBtn);

        // Codigo de barras
        add(codBarrasLbl,"align label");
        codBarras.setEditable(false);
        codBarras.setBackground(Color.LIGHT_GRAY);
        add(codBarras,"wrap");

        // Nome do kit
        add(nomeKitLbl,"align label");
        nomeKit.setDocument(new JTextFieldLimit(30));
        nomeKit.setEditable(false);
        nomeKit.setBackground(Color.LIGHT_GRAY);
        add(nomeKit,"span");

        // Tipo
        add(tipoLbl,"align label");
        tipoComboBox.addItem("Autoclave");
        tipoComboBox.addItem("Peróxido de Hidrogênio");
        tipoComboBox.addItem("Desinfecção de Alto Nível");
        tipoComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getID() == ItemEvent.ITEM_STATE_CHANGED ) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        switch (e.getItem().toString()) {
                            case "Autoclave":
                                alternarLinhaTemperatura(true,"");
                                break;
                            case "Peróxido de Hidrogênio":
                                alternarLinhaTemperatura(false,"Peróxido de Hidrogênio");
                                break;
                            case "Desinfecção de Alto Nível":
                                alternarLinhaTemperatura(false,"Desinfecção de Alto Nível");
                                break;
                        }
                    }
                }
            }
        });
        add(tipoComboBox,"span");

         // Lote
        add(loteLbl,"align label");
        loteComboBox.addItem("");
        loteComboBox.addItem("1");
        loteComboBox.addItem("2");
        loteComboBox.addItem("3");
        loteComboBox.addItem("4");
        loteComboBox.addItem("5");
        loteComboBox.addItem("6");
        loteComboBox.addItem("7");
        loteComboBox.addItem("8");
        loteComboBox.addItem("9");
        loteComboBox.addItem("10");
        loteComboBox.addItem("11");
        loteComboBox.addItem("12");
        loteComboBox.addItem("13");
        loteComboBox.addItem("14");
        loteComboBox.addItem("15");
        loteComboBox.setMaximumRowCount(loteComboBox.getModel().getSize());
        loteComboBox.setMaximumSize(new Dimension(96,24));
        loteComboBox.setEditable(true);
        add(loteComboBox,"split 3");

        // Ciclo
        add(cicloLbl,"align label");
        ciclo.setDocument(new JTextFieldLimit(8));
        add(ciclo,"wrap");

        // Esterelizacao
        add(esterelizacaoLbl,"align label");
        Date esterelizacaoDateObj = Util.convertLocalDateToDate(LocalDate.now());
        dataEsterelizacao.setDate(esterelizacaoDateObj);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dataEsterelizacao.getDateEditor();
        editor.setEditable(true);
        editor.setEnabled(true);
        add(dataEsterelizacao,"split 3");

        // Validade
        Date validadecaoDateObj = Util.convertLocalDateToDate(LocalDate.now().plusMonths(4));
        add(validadeLbl,"align label");
        dataValidade.setDate(validadecaoDateObj);
        editor = (JTextFieldDateEditor) dataValidade.getDateEditor();
        editor.setEditable(true);
        editor.setEnabled(true);
        add(dataValidade,"wrap");

        // Temperatura
        add(temperaturaLbl,"align label");
        temperaturaComboBox.addItem("134");
        temperaturaComboBox.addItem("121");
        temperaturaComboBox.setEditable((true));
        add(temperaturaComboBox,"split 3");
//
        // Temperatura Tipo
        temperaturaTipoComboBox.addItem("Autoclave");
        add(temperaturaTipoComboBox);

        // Temperatura valor
        temperaturaValorComboBox.addItem("01");
        temperaturaValorComboBox.addItem("02");
        temperaturaValorComboBox.addItem("03");
        temperaturaValorComboBox.addItem("04");
        add(temperaturaValorComboBox,"wrap");

        // Outras
        add(outrasLbl,"align label");
        outrasComboBox.addItem("");

        add(outrasComboBox,"span");
        outrasLbl.setVisible(false);
        outrasComboBox.setVisible(false);

        // Responsável preparo
        add(responsavelPreparoLbl);
        responsavelPreparoComboBox.setEditable(true);
        add(responsavelPreparoComboBox,"split 3");

        // Quantidade
        add(quantidadeLbl,"align label");
        quantidade.setDocument(new JTextFieldLimit(3));
        add(quantidade,"wrap");

        // Responsável Esterelização
        add(responsavelEsterelizacaoLbl,"align label");
        responsavelEsterelizacaoComboBox.setEditable(true);
        responsavelEsterelizacaoComboBox.setMaximumRowCount(responsavelEsterelizacaoComboBox.getModel().getSize());
        add(responsavelEsterelizacaoComboBox,"split 3");

        // Senha responsável esterelizção
        add(senhaLbl,"align label");
        senhaTxtField.setDocument(new JTextFieldLimit(10));
        add(senhaTxtField,"wrap");

        // Observacao
        add(observacaoLbl,"align label");
        observacao.setDocument(new JTextFieldLimit(28));
        add(observacao);

        this.revalidate();
        this.repaint();
    }


    public void clearForm() {

        idKit.setText("");
        codBarras.setText("");
        nomeKit.setText("");
        loteComboBox.setSelectedItem("");
        ciclo.setText("");
        quantidade.setText("");
        observacao.setText("");
        senhaTxtField.setText("");
    }

    public boolean validateForm() {

        // Empresa
        if (Objects.isNull(empresaComboBox.getSelectedItem())) {
            Notifications.showAlertMessage("Nenhuma empresa selecionada");
            return false;
        }

        if (Strings.isNullOrEmpty(empresaComboBox.getSelectedItem().toString().trim()) ||
                empresaComboBox.getSelectedItem().toString().length() > 30) {
            Notifications.showAlertMessage("Nenhuma empresa selecionada");
            return false;
        }
        if (Strings.isNullOrEmpty(empresaComboBox.getSelectedItem().toString()) ||
                empresaComboBox.getSelectedItem().toString().length() > 30) {
            Notifications.showAlertMessage("O nome da empresa conter entre 1 e 30 caracteres");
            return false;
        }

        // Kit
        if (Strings.isNullOrEmpty(nomeKit.getText().trim()) ||
                Strings.isNullOrEmpty(codBarras.getText().trim()) ||
                Strings.isNullOrEmpty(idKit.getText().trim())
                ){
            Notifications.showAlertMessage("Nenhum kit selecionado");
            return false;
        }

        // Tipo
        if (Strings.isNullOrEmpty(tipoComboBox.getSelectedItem().toString().trim())) {
            Notifications.showAlertMessage("O tipo não foi selecionado");
            return false;
        }

        // Lote
        if (Strings.isNullOrEmpty(loteComboBox.getSelectedItem().toString().trim()) ||
             loteComboBox.getSelectedItem().toString().length() > 8
        ) {
            Notifications.showAlertMessage("O lote deve ser um valor positivo de no máximo 8 dígitos");
            return false;
        }

        // Ciclo
        if (Strings.isNullOrEmpty(ciclo.getText().trim()) ||
                ciclo.getText().trim().length() > 8 ||
                ciclo.getText().trim().length() == 0
        ) {
            Notifications.showAlertMessage("O ciclo deve ser um valor positivo de no máximo 8 dígitos");
            return false;
        }
        try {
            Integer.parseInt(ciclo.getText().trim());
        } catch (NumberFormatException exception) {
            Notifications.showAlertMessage("O ciclo deve ser um valor numérico");
            return false;
        }

        // Tipo: Temperatura
        switch (tipoComboBox.getSelectedItem().toString().trim()) {

            case "Autoclave":
                if (Strings.isNullOrEmpty(temperaturaComboBox.getSelectedItem().toString().trim())) {
                    Notifications.showAlertMessage("Temperatura não informada");
                    return false;
                }
                if (Strings.isNullOrEmpty(temperaturaTipoComboBox.getSelectedItem().toString().trim())) {
                    Notifications.showAlertMessage("Tipo da temperatura não informada");
                    return false;
                }
                if (Strings.isNullOrEmpty(temperaturaValorComboBox.getSelectedItem().toString().trim())) {
                    Notifications.showAlertMessage("Valor da temperatura não informado");
                    return false;
                }
                break;

            case "Peróxido de Hidrogênio":
            case "Desinfecção de Alto Nível":
                if (Strings.isNullOrEmpty(outrasComboBox.getSelectedItem().toString().trim())) {
                    Notifications.showAlertMessage("Selecione uma opção para o campo Outras");
                    return false;
                }
                break;
        }

        // Data da validade
        LocalDate dataValidadeConvertida = Util.convertDateToLocalDate(dataValidade.getDate());
        if (dataValidadeConvertida.isBefore(LocalDate.now())) {
            Notifications.showAlertMessage("A data de validade não pode ser no passado");
            return false;
        }

        // Data da esterelização
        LocalDate dataEsterelizacaoConvertida = Util.convertDateToLocalDate(dataEsterelizacao.getDate());
        if (dataEsterelizacaoConvertida.isBefore(LocalDate.now())) {
            Notifications.showAlertMessage("A data da esterelização não pode ser no passado");
            return false;
        }

        // Responsável pela esterelização
        String nomeEsterelizador;
        int posicaoBarra = responsavelEsterelizacaoComboBox.getSelectedItem().toString().indexOf("/");
        if (posicaoBarra == -1) {
            nomeEsterelizador = responsavelEsterelizacaoComboBox.getSelectedItem().toString().trim();
        } else {
            nomeEsterelizador = responsavelEsterelizacaoComboBox.getSelectedItem().toString().trim().substring(0,posicaoBarra);
        }
        if (Strings.isNullOrEmpty(nomeEsterelizador) ||nomeEsterelizador.length() > 10) {
            Notifications.showAlertMessage("O responsável da esterelização deve conter entre 1 e 10 caracteres");
            return false;
        }

        // Senha responsável esterelização
        if (senhaTxtField.getText().length() < 5 || senhaTxtField.getText().length() > 10) {
            Notifications.showAlertMessage("A senha deve conter entre 5 e 10 caracteres");
            return false;
        }


        // Responsável pelo preparo
        String nomePreparador;
        int posicaoBarra2 = responsavelPreparoComboBox.getSelectedItem().toString().indexOf("/");
        if (posicaoBarra2 == -1) {
            nomePreparador = responsavelPreparoComboBox.getSelectedItem().toString().trim();
        } else {
            nomePreparador = responsavelPreparoComboBox.getSelectedItem().toString().trim().substring(0,posicaoBarra2);
        }
        if (Strings.isNullOrEmpty(nomePreparador) ||nomePreparador.length() > 10) {
            Notifications.showAlertMessage("O responsável do preparo deve conter entre 1 e 10 caracteres");
            return false;
        }

        // Quantidade
        if(Strings.isNullOrEmpty(quantidade.getText().trim())) {
            Notifications.showAlertMessage("Quantidade  não informada");
            return false;
        } else {
            try {

                Integer parsed = Integer.parseInt(quantidade.getText().trim());
                if(parsed <= 0) {
                    Notifications.showAlertMessage("Quantidade deve ser um valor positivo");
                    return false;
                }

            } catch (NumberFormatException exception) {
                Notifications.showAlertMessage("A quantidade deve ser um valor numérico");
                return false;
            }
        }

        return true;
    }

    public Etiqueta getEntityFromForm() {


        int posicaoBarra = responsavelPreparoComboBox.getSelectedItem().toString().indexOf("/");
        String responsavelPreparo;
        if (posicaoBarra != -1) {
             responsavelPreparo = responsavelPreparoComboBox.getSelectedItem().toString().trim().substring(0, posicaoBarra);
        } else {
            responsavelPreparo = responsavelPreparoComboBox.getSelectedItem().toString().trim();
        }

        posicaoBarra = responsavelEsterelizacaoComboBox.getSelectedItem().toString().indexOf("/");
        String responsavelEsterelizacao;
        if (posicaoBarra != -1) {
            responsavelEsterelizacao = responsavelEsterelizacaoComboBox.getSelectedItem().toString().trim().substring(0, posicaoBarra);
        } else {
            responsavelEsterelizacao = responsavelEsterelizacaoComboBox.getSelectedItem().toString().trim();
        }

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setIdKit(Integer.parseInt(idKit.getText()));
        etiqueta.setEmpresa(empresaComboBox.getSelectedItem().toString());
        etiqueta.setNomeKit(nomeKit.getText());
        etiqueta.setCodBarras(codBarras.getText());
        etiqueta.setTipo(tipoComboBox.getSelectedItem().toString());
        etiqueta.setLote(loteComboBox.getSelectedItem().toString());
        etiqueta.setCiclo(ciclo.getText());
        etiqueta.setEsterelizacao(Util.convertDateToLocalDate(dataEsterelizacao.getDate()));
        etiqueta.setValidade(Util.convertDateToLocalDate(dataValidade.getDate()));
        etiqueta.setResponsavelEsterelizacao(responsavelEsterelizacao);
        etiqueta.setOutras(outrasComboBox.getSelectedItem().toString());
        etiqueta.setResponsavelPreparo(responsavelPreparo);
        etiqueta.setQuantidade(Integer.parseInt(quantidade.getText()));
        etiqueta.setObservacao(observacao.getText());
        etiqueta.setTipoEnvolucro(tipoEnvolucro);
        etiqueta.setEnvolucro(envolucro);
        etiqueta.setTotalEnvolucro(totalEnvolucro);
        etiqueta.setTamanho(tamanhoDoKit);

        if (Objects.equals(etiqueta.getTipo(), "Autoclave")) {
            etiqueta.setTemperatura(temperaturaComboBox.getSelectedItem().toString());
            etiqueta.setTemperaturaTipo(temperaturaTipoComboBox.getSelectedItem().toString());
            etiqueta.setTemperaturaValor(temperaturaValorComboBox.getSelectedItem().toString());
            etiqueta.setOutras("");
        } else {
            etiqueta.setTemperatura("");
            etiqueta.setTemperaturaTipo("");
            etiqueta.setTemperaturaValor("");
            etiqueta.setOutras(outrasComboBox.getSelectedItem().toString());
        }

        return etiqueta;
    }

}
