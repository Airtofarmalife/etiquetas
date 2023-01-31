package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.domain.CustomJRViewer;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.enumeration.JasperReportType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.etiqueta.EtiquetaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.jasper.PdfService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.kit.KitService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.LoginService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.searchkit.SearchKitJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.AddEtiquetaJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.EtiquetaFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.view.modal.EtiquetaFormPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@AllArgsConstructor
public class EtiquetaController extends AbstractFrameController {

    private final AddEtiquetaJdialog addEtiquetaJdialog;
    private final SearchKitJdialog searchKitJdialog;
    private final EtiquetaService etiquetaService;
    private final KitService kitService;

    private final LoginService loginService;

    @Autowired
    private PdfService pdfService;

    @PostConstruct
    private void prepareListeners() {

        EtiquetaFormBtnPanel formBtnPanel = addEtiquetaJdialog.getEtiquetaFormBtnPanel();

        // Visualizar janela de etiqueta
        registerAction(formBtnPanel.getPrintBtn(), (e) -> {
            try {
                EtiquetaFormPanel formPanel = addEtiquetaJdialog.getEtiquetaFormPanel();
                if (formPanel.validateForm()) {

                    String nomeFormulario = formPanel.getResponsavelEsterelizacaoComboBox().getSelectedItem().toString().trim();
                    String senha = formPanel.getSenhaTxtField().getText();

                    String nome;
                    String nomeComplementar = "";
                    int posicaoBarra = nomeFormulario.indexOf("/");
                    if (posicaoBarra != -1) {
                         nome = nomeFormulario.substring(0,posicaoBarra);
                         nomeComplementar = nomeFormulario.substring(posicaoBarra+1);
                    } else {
                         nome = nomeFormulario;
                    }

                    boolean credenciaisCorretas = loginService.verificarCredenciais(nome,senha,nomeComplementar);
                    if (credenciaisCorretas) {
                        visualizarEtiqueta();
                    }

                    //  closeModalWindow();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Fechar este frame
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeModalWindow());


        EtiquetaFormPanel etiquetaFormPanel = addEtiquetaJdialog.getEtiquetaFormPanel();

        // Procurar kit
        registerAction(etiquetaFormPanel.getProcurarKitBtn(), (e) -> {
            searchKitJdialog.setAlwaysOnTop(true);
            searchKitJdialog.setVisible(true);
        });

        // Escolher o kit
        registerAction(searchKitJdialog.getSearchKitJpanel().getSelecionarBtn(), (e) -> {

            try {
                int row = searchKitJdialog.getSearchKitJpanel().getJTable().getSelectedRow();
                javax.swing.table.TableModel tableModel = searchKitJdialog.getSearchKitJpanel().getJTable().getModel();
                JTable jTable = searchKitJdialog.getSearchKitJpanel().getJTable();
                String id = tableModel.getValueAt(jTable.convertRowIndexToModel(row), 0).toString();

                Kit kit = kitService.findById(Integer.parseInt(id));

                etiquetaFormPanel.getIdKit().setText(String.valueOf(kit.getId()));
                etiquetaFormPanel.getNomeKit().setText(kit.getDescricao());
                etiquetaFormPanel.getCodBarras().setText(kit.getCodbarras());
                etiquetaFormPanel.getQuantidade().setText(String.valueOf(kit.getQuantidade()));
                etiquetaFormPanel.setTipoEnvolucro(kit.getTipoEnvolucro());
                etiquetaFormPanel.setEnvolucro(kit.getEnvolucro());
                etiquetaFormPanel.setTotalEnvolucro(kit.getTotalEnvolucro());
                etiquetaFormPanel.setTamanhoDoKit(kit.getTamanho().trim());


                if (!Strings.isNullOrEmpty(kit.getAnexo())) {
                    File anexo = new File(kit.getAnexo());

                    addEtiquetaJdialog.setSize(new Dimension(1100,500));
                    addEtiquetaJdialog.setPreferredSize(new Dimension(1100,500));
                    addEtiquetaJdialog.setLocationRelativeTo(null);
                    addEtiquetaJdialog.revalidate();
                    addEtiquetaJdialog.pack();
                    addEtiquetaJdialog.getEtiquetaAnexoPanel().exibirAnexo(anexo);
                    addEtiquetaJdialog.getEtiquetaAnexoPanel().setVisible(true);
                } else {
                    addEtiquetaJdialog.setSize(new Dimension(550,500));
                    addEtiquetaJdialog.setPreferredSize(new Dimension(550,500));
                    addEtiquetaJdialog.revalidate();
                    addEtiquetaJdialog.pack();
                    addEtiquetaJdialog.getEtiquetaAnexoPanel().setVisible(false);
                }

                searchKitJdialog.dispose();
            } catch (ArrayIndexOutOfBoundsException exception) {
                Notifications.showNonRowSelected();
            }
        });

        registerAction(addEtiquetaJdialog.getEtiquetaAnexoPanel().getShowPdfBtn(), (e) -> exibirPdfAnexo());
    }


    @Override
    public void prepareAndOpenFrame() {
        showAddModal();
    }

    private void showAddModal() {
        addEtiquetaJdialog.setSize(new Dimension(550,500));
        addEtiquetaJdialog.setPreferredSize(new Dimension(550,500));
        addEtiquetaJdialog.setLocationRelativeTo(null);
        addEtiquetaJdialog.getEtiquetaAnexoPanel().setVisible(false);
        addEtiquetaJdialog.getEtiquetaFormPanel().clearForm();
        addEtiquetaJdialog.getEtiquetaFormPanel().loadEmpresas();
        addEtiquetaJdialog.getEtiquetaFormPanel().loadResponsaveis();
        addEtiquetaJdialog.setVisible(true);
    }

    private void visualizarEtiqueta() throws Exception {

        EtiquetaFormPanel formPanel = addEtiquetaJdialog.getEtiquetaFormPanel();
        Etiqueta entity = formPanel.getEntityFromForm();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        String textoDataEsterelizacao = entity.getEsterelizacao().format(formatter);
        String textoDataValidade = entity.getEsterelizacao().format(formatter);

        String linhaAutoClave;
        if (Objects.equals(entity.getTipo(), "Autoclave")) {
            linhaAutoClave = entity.getTemperaturaTipo() + ": " + entity.getTemperaturaValor() + " - Temp:" + entity.getTemperatura() + "º";
        } else {
            linhaAutoClave = entity.getOutras().toUpperCase();
        }

        // Código de barras
        String barcode = etiquetaService.generateEAN13BarcodeImageBase64(entity.getCodBarras());

        // Hora da geracão
        LocalDateTime dataAtualDateTime = LocalDateTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

        String preparador;
        int posicaoBarraPreparador = entity.getResponsavelPreparo().indexOf("/");
        if (posicaoBarraPreparador == -1) {
            preparador = entity.getResponsavelPreparo();
        } else {
            preparador = entity.getResponsavelPreparo().substring(0,entity.getResponsavelPreparo().indexOf("/"));
        }

        String esterelizador;
        int posicaoBarraEsterelizador = entity.getResponsavelEsterelizacao().indexOf("/");
        if (posicaoBarraEsterelizador == -1) {
            esterelizador = entity.getResponsavelEsterelizacao();
        } else {
            esterelizador = entity.getResponsavelEsterelizacao().substring(0,entity.getResponsavelEsterelizacao().indexOf("/"));
        }

        String finalLinhaAutoClave = linhaAutoClave;
        Map<String, Object> content = new HashMap<String, Object>() {{
            put("EMPRESA", entity.getEmpresa());
            put("KIT", entity.getNomeKit());
            put("CICLO", "Ciclo: " + entity.getCiclo() + " Lote " + entity.getLote());
            put("AUTO_CLAVE", finalLinhaAutoClave);
            put("RESPONSAVEIS", "Prep:" + preparador + " - Est:" + esterelizador);
            put("HORA",  dataAtualDateTime.format(formatterTime));
            put("QUANTIDADE", "PC:" + entity.getQuantidade() + " Data:" + textoDataEsterelizacao + " Val:" + textoDataValidade);
            put("OBS", entity.getObservacao());
            put("BAR_CODE_IMAGE", barcode);
        }};

        JasperPrint jasperPrint = pdfService.generateToScreen(JasperReportType.ETIQUETA_STATEMENT, content);
        CustomJRViewer jrViewer = new CustomJRViewer(jasperPrint);
        jrViewer.getActionMap();
        jrViewer.setZoomRatio(2);
        jrViewer.setFitPageZoomRatio();
        jrViewer.setEtiquetaController(this);

        JFrame applicationFrame = new JFrame();
        applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        applicationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                addEtiquetaJdialog.setVisible(true);
            }
        });
        applicationFrame.add(jrViewer);
        applicationFrame.setSize(new Dimension(650,500));
        applicationFrame.setPreferredSize(new Dimension(650,500));
        applicationFrame.setLocationRelativeTo(null);
        applicationFrame.setAlwaysOnTop(true);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
        addEtiquetaJdialog.setVisible(false);
    }

    public void saveEntity() {
        EtiquetaFormPanel formPanel = addEtiquetaJdialog.getEtiquetaFormPanel();
        Etiqueta entity = formPanel.getEntityFromForm();
        etiquetaService.save(entity);
    }

    private void exibirPdfAnexo() {
        File arquivo = new File(addEtiquetaJdialog.getEtiquetaAnexoPanel().getFile().getAbsolutePath());
        try {
            Desktop.getDesktop().open(arquivo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeModalWindow() {
        addEtiquetaJdialog.dispose();
    }
}


