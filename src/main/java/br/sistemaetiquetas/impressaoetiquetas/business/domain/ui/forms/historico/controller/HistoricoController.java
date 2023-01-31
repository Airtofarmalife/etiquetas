package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration.JasperReportType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.etiqueta.EtiquetaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.jasper.PdfService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.periodo.PeriodoService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto.*;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.enumeration.EnumTurnosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.searchEnvolucro.SearchEnvolucroJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.HistoricoFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.HistoricoFormPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.view.modal.HistoricoJDialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.Util;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoricoController extends AbstractFrameController {

    @Autowired
    private EtiquetaService etiquetaService;

    @Autowired
    private PeriodoService periodoService;

    @Autowired
    private PdfService pdfService;

    private final HistoricoJDialog historicoJDialog;

    @Autowired
    private SearchEnvolucroJdialog searchEnvolucroJdialog;

    private static final String tempFilePath = "C:\\etiquetas\\temp\\relatorio.pdf";

    @Override
    public void prepareAndOpenFrame() {
        showAddModal();
    }

    private void showAddModal() {
        historicoJDialog.getHistoricoFormPanel().loadEmpresas();
        historicoJDialog.getHistoricoFormPanel().loadPeriodos();
        historicoJDialog.setVisible(true);
    }

    private void closeModalWindow() {
        historicoJDialog.getHistoricoFormPanel().clearForm();
        historicoJDialog.dispose();
    }

    @PostConstruct
    private void prepareListeners() {
        HistoricoFormBtnPanel formBtnPanel = historicoJDialog.getHistoricoFormBtnPanel();

        registerAction(formBtnPanel.getPrintBtn(), (e) -> gerarRelatorio());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeModalWindow());
        registerAction(historicoJDialog.getHistoricoFormPanel().getEnvolucrosButton(), (e) -> {
            String sms = historicoJDialog.getHistoricoFormPanel().getTiposEnvolucroCombobox().getSelectedItem().toString();
            searchEnvolucroJdialog.setAlwaysOnTop(true);
            searchEnvolucroJdialog.setEnvolucroTipo(sms);
            searchEnvolucroJdialog.setVisible(true);
        });

        // Confirma seleção dos envólucros
        registerAction(searchEnvolucroJdialog.getSearchEnvolucroJpanel().getSelecionarBtn(), (e) -> {

            HistoricoFormPanel historicoFormPanel = historicoJDialog.getHistoricoFormPanel();

            try {
                // Adicionar no form
                ArrayList<String> envolucros = new ArrayList<>();
                int[] selection = searchEnvolucroJdialog.getSearchEnvolucroJpanel().getJTable().getSelectedRows();
                for (int index : selection) {
                    String envolucro = (String) searchEnvolucroJdialog.getSearchEnvolucroJpanel().getJTable().getValueAt(index,0);
                    envolucros.add(envolucro);
                }
                historicoFormPanel.getEnvolucros().clear();
                historicoFormPanel.getEnvolucros().addAll(envolucros);

                // Adiciona na text area
                JTextArea envolucroTextArea = historicoFormPanel.getEnvolucroTextArea();
                envolucroTextArea.setText("");
                envolucros.forEach(v -> envolucroTextArea.append(v + "\n"));

                searchEnvolucroJdialog.setVisible(false);

            } catch (ArrayIndexOutOfBoundsException exception) {
                Notifications.showNonRowSelected();
            }
        });
    }

    private void gerarRelatorio() {

        HistoricoFormPanel historicoFormPanel = historicoJDialog.getHistoricoFormPanel();
        if(!historicoFormPanel.validateForm()) { return; }

        LocalDate localDateInicial = Util.convertDateToLocalDate(historicoFormPanel.getDiaInicialChooser().getDate());
        LocalDate localDateFinal = Util.convertDateToLocalDate(historicoFormPanel.getDiaFinalChooser().getDate());
        LocalTime localTimeInicial = LocalTime.parse(historicoFormPanel.getHoraInicialTextField().getText().trim());
        LocalTime localTimeFinal = LocalTime.parse(historicoFormPanel.getHoraFinalTextField().getText().trim());

        // Livre
        boolean relatorioLivre = historicoFormPanel.getLivreCheckbox().getState();
        if (relatorioLivre) {
            gerarRelatorioLivre(localDateInicial, localTimeInicial, localDateFinal, localTimeFinal);
            return;
        }

        // Por envólucros
        boolean relatorioEnvolucro = historicoFormPanel.getEnvolucrosCheckbox().getState();
        if (relatorioEnvolucro) {

            ArrayList<String> envolucros = historicoFormPanel.getEnvolucros();

            if (envolucros.size() == 0) {
                Notifications.showAlertMessage("Nenhum envólucro selecionado.");
                return;
            }
            gerarRelatorioPorEnvolucros(localDateInicial,localDateFinal,envolucros);
        }

        // Por turnos
        boolean relatorioTurnos = historicoFormPanel.getTurnosCheckbox().getState();
        if (relatorioTurnos) {
            List<String> turnos = new ArrayList<>();
            JComboBox turnosComboBox = historicoFormPanel.getPeriodoComboBox();
            String turnoSelecionado = turnosComboBox.getSelectedItem().toString();
            if (Objects.equals(turnoSelecionado,"Dia")) {
                for (int i = 0; i < turnosComboBox.getItemCount(); i++) {
                    String turno = turnosComboBox.getItemAt(i).toString();
                    if (!turno.equals("Dia")) {
                        turnos.add(turno);
                    }
                }
            } else {
                turnos.add(turnoSelecionado);
            }

            // Gerar relatório por turno
            gerarRelatorioTurnos(localDateInicial, localDateFinal,turnos);
        }

    }

    private void gerarRelatorioTurnos(LocalDate localDateInicial, LocalDate localDateFinal, List<String> turnos) {

        HistoricoFormPanel historicoFormPanel = historicoJDialog.getHistoricoFormPanel();
        String tipo = historicoFormPanel.getTipoComboBox().getSelectedItem().toString();
        String empresa = historicoFormPanel.getEmpresaComboBox().getSelectedItem().toString();

        HashMap<String, HistoricoKitsPorTurno> hashMapKitsPorTurno = new HashMap<>();

        for (String turno : turnos) {
            List<Etiqueta> etiquetasResult = new ArrayList<>();

            if (Objects.equals(turno,"Noite")) {
                Periodo noite = periodoService.findByNome("Noite");
                LocalDate localDateInicialAux = localDateInicial;

                while ( localDateInicialAux.isBefore(localDateFinal.plusDays(1)) ) {
                    LocalDateTime localDateTimeInicial = LocalDateTime.of(localDateInicialAux,noite.getHoraInicial());  // 01/01 19:01
                    localDateInicialAux = localDateInicialAux.plusDays(1);
                    LocalDateTime localDateTimeFinal = LocalDateTime.of(localDateInicialAux,noite.getHoraFinal()); // 02/01 07:00
                    etiquetasResult.addAll(etiquetaService.findByInterval(localDateTimeInicial,localDateTimeFinal));
                }

            } else {

                etiquetasResult = etiquetaService.findReportData(localDateInicial, localDateFinal, turno);
            }

            for (Etiqueta etiqueta : etiquetasResult) {

                if (!Strings.isNullOrEmpty(empresa) && !Objects.equals(etiqueta.getEmpresa(), empresa)) { continue; }
                if ( !Strings.isNullOrEmpty(tipo) && !Objects.equals(etiqueta.getTipo(), tipo)) { continue; }

                String nomeKit = etiqueta.getNomeKit();
                if (hashMapKitsPorTurno.containsKey(nomeKit)) {
                    HistoricoKitsPorTurno historicoKitsPorTurno = hashMapKitsPorTurno.get(nomeKit);
                    incrementByTurn(historicoKitsPorTurno, turno);

                } else {

                    HistoricoKitsPorTurno historicoKitsPorTurno = HistoricoKitsPorTurno.builder()
                            .nomeKit(nomeKit)
                            .idKit(etiqueta.getIdKit())
                            .tamanho(etiqueta.getTamanho())
                            .build();
                    incrementByTurn(historicoKitsPorTurno, turno);
                    hashMapKitsPorTurno.put(nomeKit, historicoKitsPorTurno);
                }
            }
        }

        if (hashMapKitsPorTurno.size() == 0) {
            Notifications.showAlertMessage("Nenhuma etiqueta gerada para o período informado.");
            return;
        }

        HistoricoKitsPorTurnoWrapper historicoKitTurnoWrapper = new HistoricoKitsPorTurnoWrapper();
        List<HistoricoKitsPorTurno> listKitsPorTurno = new ArrayList<>(hashMapKitsPorTurno.values());
        historicoKitTurnoWrapper.setHistoricoKitsPorTurnoList(listKitsPorTurno);

        // Empresas
        StringJoiner empresas = new StringJoiner(", ");
        JComboBox empresaCombobox = historicoFormPanel.getEmpresaComboBox();
        for (int i=1; i < empresaCombobox.getItemCount(); i++) {
            empresas.add(empresaCombobox.getItemAt(i).toString());
        }

        // Tipos
        StringJoiner tipos = new StringJoiner(", ");
        JComboBox tipoComboBox = historicoFormPanel.getTipoComboBox();
        if (tipoComboBox.getSelectedItem() != "" && tipoComboBox.getSelectedItem() != null) {
            tipos.add(tipoComboBox.getSelectedItem().toString());
        } else {
            for (int i=1; i < tipoComboBox.getItemCount(); i++) {
                tipos.add(tipoComboBox.getItemAt(i).toString());
            }
        }

        // Formatters
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

        // Data atual
        LocalDateTime dataAtualDateTime = LocalDateTime.now();

        Map<String, Object> content = new HashMap<String, Object>() {{
            put("PERIODO_INICIO", "Período de " + localDateInicial.format(formatter) + " a " + localDateFinal.format(formatter));
            put("DATA_ATUAL", "Emissâo: " + dataAtualDateTime.format(formatterTime));
            put("EMPRESAS", "Empresa(s): " + empresas);
            put("TIPOS", "Tipo(s): " + tipos);
            put("TOTAL_MANHA" , String.valueOf(historicoKitTurnoWrapper.getTotalManha()));
            put("TOTAL_TARDE" , String.valueOf(historicoKitTurnoWrapper.getTotalTarde()));
            put("TOTAL_NOITE" , String.valueOf(historicoKitTurnoWrapper.getTotalNoite()));
            put("TOTAL_PORTE" , String.valueOf(historicoKitTurnoWrapper.getTotalTurnos()));
        }};

        ArrayList<HashMap<String,String>> statements = new ArrayList<>();
        historicoKitTurnoWrapper.getHistoricoKitsPorTurnoList().forEach(e ->
                statements.add(new HashMap<String,String >() {{
                      put("KIT_ID", String.valueOf(e.getIdKit()));
                      put("KIT_NOME", e.getNomeKit());
                      put("KIT_TOTAL_MANHA", turnos.contains("Manhã") ? String.valueOf(e.getTotalManha()) : "-");
                      put("KIT_TOTAL_TARDE", turnos.contains("Tarde") ? String.valueOf(e.getTotalTarde()) : "-");
                      put("KIT_TOTAL_NOITE", turnos.contains("Noite") ? String.valueOf(e.getTotalNoite()) : "-");
                      put("KIT_TOTAL_PORTE", String.valueOf(e.getPorte()));
                }})
        );
        content.put("STATEMENTS", statements);

        StringJoiner totaisPorTamanho = new StringJoiner(" ");
        historicoKitTurnoWrapper.getTotalPorTamanho().forEach( (e, v) ->
                totaisPorTamanho.add(e + ": " + v)
        );
        content.put("TOTAIS_POR_TAMANHO",totaisPorTamanho.toString());

        exibirRelatorio(JasperReportType.RELATORIO_TURNOS,content);
    }

    private void gerarRelatorioLivre(LocalDate localDateInicial, LocalTime localTimeInicial, LocalDate localDateFinal, LocalTime localTimeFinal ) {

        HistoricoFormPanel historicoFormPanel = historicoJDialog.getHistoricoFormPanel();
        LocalDateTime inicio = LocalDateTime.of(localDateInicial,localTimeInicial);
        LocalDateTime fim = LocalDateTime.of(localDateFinal,localTimeFinal);

        String tipo = historicoFormPanel.getTipoComboBox().getSelectedItem().toString();
        String empresa = historicoFormPanel.getEmpresaComboBox().getSelectedItem().toString();

        HashMap<String, HistoricoKitsPorPeriodo> hashMapKitsPorPeriodo = new HashMap<>();

        List<Etiqueta> etiquetasResult = etiquetaService.findByInterval(inicio,fim);
        if (etiquetasResult.size() == 0) {
            Notifications.showAlertMessage("Nenhuma etiqueta gerada para o período informado.");
            return;
        }

        for (Etiqueta etiqueta : etiquetasResult) {

            if (!Strings.isNullOrEmpty(empresa) &&!Objects.equals(etiqueta.getEmpresa(), empresa)) { continue; }
            if ( !Strings.isNullOrEmpty(tipo) && !Objects.equals(etiqueta.getTipo(), tipo)) { continue; }

            if (hashMapKitsPorPeriodo.containsKey(etiqueta.getNomeKit())) {
                HistoricoKitsPorPeriodo historicoKitsPorPeriodo = hashMapKitsPorPeriodo.get(etiqueta.getNomeKit());
                historicoKitsPorPeriodo.incrementQuantidade();

            } else {

                HistoricoKitsPorPeriodo historicoKitsPorPeriodo =
                        HistoricoKitsPorPeriodo.builder()
                                .idKit(etiqueta.getIdKit())
                                .nomeKit(etiqueta.getNomeKit())
                                .responsavel(etiqueta.getResponsavelEsterelizacao())
                                .lote(etiqueta.getLote())
                                .ciclo(etiqueta.getCiclo())
                                .temperaturaValor(etiqueta.getTemperaturaValor())
                                .quantidade(1)
                                .tamanho(etiqueta.getTamanho())
                                .build();
                hashMapKitsPorPeriodo.put(etiqueta.getNomeKit(),historicoKitsPorPeriodo);
            }
        }

        // Empresas
        StringJoiner empresas = new StringJoiner(", ");
        JComboBox empresaCombobox = historicoFormPanel.getEmpresaComboBox();
        for (int i=1; i < empresaCombobox.getItemCount(); i++) {
            empresas.add(empresaCombobox.getItemAt(i).toString());
        }

        // Tipos
        StringJoiner tipos = new StringJoiner(", ");
        JComboBox tipoComboBox = historicoFormPanel.getTipoComboBox();
        if (tipoComboBox.getSelectedItem() != "" && tipoComboBox.getSelectedItem() != null) {
            tipos.add(tipoComboBox.getSelectedItem().toString());
        } else {
            for (int i=1; i < tipoComboBox.getItemCount(); i++) {
                tipos.add(tipoComboBox.getItemAt(i).toString());
            }
        }

        // Periodo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDateTime dataAtualDateTime = LocalDateTime.now();

        String cancelado = "";
        if (historicoFormPanel.getRelatorioCanceladoCheckbox().getState()) {
            cancelado = "ABORTADO";
        }

        String finalCancelado = cancelado;
        Map<String, Object> content = new HashMap<String,Object>() {{
                put("CANCELADO", finalCancelado);
                put("PERIODO_INICIO", "Período de " + inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyy")) +
                        " a " + fim.format(formatter) +
                        " - " + inicio.format(formatterTime) + " à " + fim.format(formatterTime));
                put("DATA_ATUAL", "Emissâo: " + dataAtualDateTime.format(formatterDateTime));
                put("EMPRESAS", "Empresa(s): " + empresas.toString());
                put("TIPOS", "Tipo(s): " + tipos.toString());
                put("FILTRO_HORARIO", "Período selecionado: " + inicio.format(formatterDateTime) + " à " + fim.format(formatterDateTime));
        }};

        List<HistoricoKitsPorPeriodo> historicoKitsPorPeriodos = new ArrayList<>(hashMapKitsPorPeriodo.values());
        HistoricoKitsPorPeriodoWrapper historicoKitsPorPeriodoWrapper = new HistoricoKitsPorPeriodoWrapper();
        historicoKitsPorPeriodoWrapper.setHistoricoKitsPorPeriodos(historicoKitsPorPeriodos);

        ArrayList<HashMap<String,String>> statements = new ArrayList<>();
        historicoKitsPorPeriodoWrapper.getHistoricoKitsPorPeriodos().forEach((e) ->
                statements.add(new HashMap<String,String>() {{
                    put("KIT_ID",String.valueOf(e.getIdKit()));
                    put("KIT_NOME", e.getNomeKit());
                    put("KIT_QUANTIDADE",String.valueOf(e.getQuantidade()));
                    put("KIT_LOTE",String.valueOf(e.getLote()));
                    put("KIT_CICLO",String.valueOf(e.getCiclo()));
                    put("KIT_AUTOCLAVE",String.valueOf(e.getTemperaturaValor()));
                    put("KIT_RESPONSAVEL",String.valueOf(e.getResponsavel()));
                }})
        );

        content.put("TOTAL", String.valueOf(historicoKitsPorPeriodoWrapper.getTotal()) );
        content.put("STATEMENTS",statements);

        StringJoiner totaisPorTamanho = new StringJoiner(" ");
        historicoKitsPorPeriodoWrapper.getTotalPorTamanho().forEach( (e, v) ->
                totaisPorTamanho.add(e + ": " + v)
        );
        content.put("TOTAIS_POR_TAMANHO",totaisPorTamanho.toString());

        exibirRelatorio(JasperReportType.RELATORIO_LIVRE,content);

    }

    private void gerarRelatorioPorEnvolucros(LocalDate inicio, LocalDate fim, ArrayList<String> envolucros) {

        HistoricoFormPanel historicoFormPanel = historicoJDialog.getHistoricoFormPanel();
        String tipo = historicoFormPanel.getTipoComboBox().getSelectedItem().toString();
        String empresa = historicoFormPanel.getEmpresaComboBox().getSelectedItem().toString();

        List<Etiqueta> etiquetas;

        String tipoEnvolucro = historicoFormPanel.getTiposEnvolucroCombobox().getSelectedItem().toString();
        if (tipoEnvolucro.length() == 0) {
            etiquetas = etiquetaService.findByIntervalAndEnvolucros(inicio, fim, envolucros);
        } else {
            etiquetas = etiquetaService.findByIntervalAndEnvolucrosWithType(inicio, fim, envolucros,tipoEnvolucro);
        }

        HashMap<String,HistoricoKitsPorEnvolucro> hashMapEnvolucrosSMS = new HashMap<>();
        HashMap<String,HistoricoKitsPorEnvolucro> hashMapEnvolucrosPgrau = new HashMap<>();

        for (Etiqueta etiqueta : etiquetas) {

            if (!Strings.isNullOrEmpty(empresa) &&!Objects.equals(etiqueta.getEmpresa(), empresa)) { continue; }
            if (!Strings.isNullOrEmpty(tipo) && !Objects.equals(etiqueta.getTipo(), tipo)) { continue; }

            String chaveEvolucro = etiqueta.getEnvolucro() + "." + etiqueta.getTotalEnvolucro();

            if (Objects.equals(etiqueta.getTipoEnvolucro(),"SMS")) {

                if (hashMapEnvolucrosSMS.containsKey(chaveEvolucro)) {
                    HistoricoKitsPorEnvolucro historicoKitsPorEnvolucro = hashMapEnvolucrosSMS.get(chaveEvolucro);
                    historicoKitsPorEnvolucro.incrementQuantidade(etiqueta.getTamanho());

                } else {

                    HistoricoKitsPorEnvolucro historicoKitsPorEnvolucro =
                            HistoricoKitsPorEnvolucro.builder()
                                    .envolucro(chaveEvolucro)
                                    .multiplicador(etiqueta.getTotalEnvolucro())
                                    .build();
                    historicoKitsPorEnvolucro.incrementQuantidade(etiqueta.getTamanho());
                    hashMapEnvolucrosSMS.put(chaveEvolucro,historicoKitsPorEnvolucro);
                }
                continue;
            }

            if (hashMapEnvolucrosPgrau.containsKey(chaveEvolucro)) {
                HistoricoKitsPorEnvolucro historicoKitsPorEnvolucro = hashMapEnvolucrosPgrau.get(chaveEvolucro);
                historicoKitsPorEnvolucro.incrementQuantidade(etiqueta.getTamanho());

            } else {

                HistoricoKitsPorEnvolucro historicoKitsPorEnvolucro =
                        HistoricoKitsPorEnvolucro.builder()
                                .envolucro(chaveEvolucro)
                                .multiplicador(etiqueta.getTotalEnvolucro())
                                .build();
                historicoKitsPorEnvolucro.incrementQuantidade(etiqueta.getTamanho());
                hashMapEnvolucrosPgrau.put(chaveEvolucro,historicoKitsPorEnvolucro);
            }

        }

        if (hashMapEnvolucrosPgrau.size() == 0 && hashMapEnvolucrosSMS.size() == 0) {
            Notifications.showAlertMessage("Nenhuma etiqueta gerada para o período informado.");
            return;
        }

        // Data atual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        LocalDateTime dataAtualDateTime = LocalDateTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

        // Empresas
        StringJoiner empresas = new StringJoiner(", ");
        JComboBox empresaCombobox = historicoFormPanel.getEmpresaComboBox();
        for (int i=1; i < empresaCombobox.getItemCount(); i++) {
            empresas.add(empresaCombobox.getItemAt(i).toString());
        }

        // Tipos
        StringJoiner tipos = new StringJoiner(", ");
        JComboBox tipoComboBox = historicoFormPanel.getTipoComboBox();
        if (tipoComboBox.getSelectedItem() != "" && tipoComboBox.getSelectedItem() != null) {
            tipos.add(tipoComboBox.getSelectedItem().toString());
        } else {
            for (int i=1; i < tipoComboBox.getItemCount(); i++) {
                tipos.add(tipoComboBox.getItemAt(i).toString());
            }
        }

        Map<String, Object> content = new HashMap<String,Object>() {{
            put("PERIODO_INICIO", "Período de " + inicio.format(formatter) + " a " + fim.format(formatter));
            put("DATA_ATUAL", "Emissâo: " + dataAtualDateTime.format(formatterTime));
            put("EMPRESAS", "Empresa(s): " + empresas);
            put("TIPOS", "Tipo(s): " + tipos);
            put("FILTROS", "Envólucros(s) utilizado(s): " + String.join(", ",envolucros));
        }};

        // Prepara SMS's
        ArrayList<HashMap<String,String>> statementsSMS = new ArrayList<>();
        Map<String,HistoricoKitsPorEnvolucro> treeMapEnvolucrosSMS = new TreeMap<>(hashMapEnvolucrosSMS);
        treeMapEnvolucrosSMS.forEach( (e,v) ->
                statementsSMS.add(new HashMap<String,String>() {{
                    put("ENVOLUCRO", v.getEnvolucro());
                    put("QUANTIDADE", String.valueOf(v.getQuantidade() * v.getMultiplicador()));
                }})
        );
        content.put("STATEMENTS_TNT",statementsSMS);

        // Prepara PGRAUs
        ArrayList<HashMap<String,String>> statmentsPgrau = new ArrayList<>();
        Map<String,HistoricoKitsPorEnvolucro> treeMapEnvolucrosPgrau = new TreeMap<>(hashMapEnvolucrosPgrau);
        treeMapEnvolucrosPgrau.forEach( (e,v) ->
                statmentsPgrau.add(new HashMap<String,String>() {{
                    put("ENVOLUCRO", v.getEnvolucro());
                    put("QUANTIDADE", String.valueOf(v.getQuantidade() * v.getMultiplicador()));
                }})
        );

        content.put("STATEMENTS_PGRAU",statmentsPgrau);

        // Cria lista com todos os kits por envólucro
        List<HistoricoKitsPorEnvolucro> kitsPorEnvolucros = new ArrayList<>();
        kitsPorEnvolucros.addAll(hashMapEnvolucrosSMS.values());
        kitsPorEnvolucros.addAll(hashMapEnvolucrosPgrau.values());

        // Adiciona lisa no wrapper
        HistoricoKitsPorEnvolucroWrapper historicoKitsPorEnvolucroWrapper = new HistoricoKitsPorEnvolucroWrapper();
        historicoKitsPorEnvolucroWrapper.setHistoricoKitsPorEnvolucros(kitsPorEnvolucros);

        // Cria a string de total por tamanhos para o relatório
        StringJoiner totaisPorTamanho = new StringJoiner(" ");
        historicoKitsPorEnvolucroWrapper.contabilizarPorTamanho().forEach( (e, v) ->
                totaisPorTamanho.add(e + ": " + v + "   ")
        );
        content.put("TOTAIS_POR_TAMANHO",totaisPorTamanho.toString());

        exibirRelatorio(JasperReportType.RELATORIO_ENVOLUCROS,content);

    }

    private void exibirRelatorio(JasperReportType jasperReportType, Map<String,Object> content) {

        JasperPrint jasperPrint = pdfService.generateToScreen(jasperReportType, content);

        JRViewer jrViewer = new JRViewer(jasperPrint);
        jrViewer.setZoomRatio(0.9f);
        jrViewer.setFitPageZoomRatio();

        JFrame applicationFrame = new JFrame();
        applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        applicationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                historicoJDialog.setVisible(true);
            }
        });
        applicationFrame.add(jrViewer);
        applicationFrame.setSize(new Dimension(750,900));
        applicationFrame.setPreferredSize(new Dimension(750,900));
        applicationFrame.setLocationRelativeTo(null);
        applicationFrame.setAlwaysOnTop(true);
        applicationFrame.pack();
        applicationFrame.setVisible(true);

        historicoJDialog.setVisible(false);
    }


    private void incrementByTurn(HistoricoKitsPorTurno historicoKitsPorTurno, String turno) {
        EnumTurnosType turnoType = EnumTurnosType.from(turno);
        switch (turnoType) {
            case MANHA:
                historicoKitsPorTurno.incrementManha();
                break;
            case NOITE:
                historicoKitsPorTurno.incrementNoite();
                break;
            case TARDE:
                historicoKitsPorTurno.incrementTarde();
                break;
        }
    }



}