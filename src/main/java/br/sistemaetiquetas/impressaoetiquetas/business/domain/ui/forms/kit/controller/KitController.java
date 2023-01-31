package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.kit.KitService;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit.AddKitJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit.KitAnexoPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit.KitFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit.KitFormPanel;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.search.ListKitJpanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.search.ListKitJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;

import com.google.common.base.Strings;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tika.Tika;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Date;

import org.springframework.stereotype.Controller;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class KitController extends AbstractFrameController {

    private final AddKitJdialog addKitJdialog;
    private final ListKitJdialog listKitJdialog;
    private final KitService kitService;

    @PostConstruct
    private void prepareListeners() {

        // Tela listagem
        ListKitJpanel searchKitJpanel = listKitJdialog.getListKitJpanel();
        registerAction(searchKitJpanel.getEdiBtn(),(e -> editEntity()));
        registerAction(searchKitJpanel.getAddBtn(),(e -> showAddModal(null)));
        registerAction(searchKitJpanel.getRemoveBtn(),(e -> removeEntity()));

        // Tela cadastro/edicao
        KitFormBtnPanel formBtnPanel = addKitJdialog.getFormBtnPanel();
        registerAction(formBtnPanel.getSaveBtn(), (e) -> saveEntity());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeAddKitJdialog());
        // Painel interno para exibir pdf ou imagem do kit
        KitAnexoPanel kitAnexoPanel = addKitJdialog.getKitAnexoPanel();
        registerAction(kitAnexoPanel.getShowPdfBtn(), (e) -> exibirPdfAnexo());
    }

    @Override
    public void prepareAndOpenFrame() {

        listKitJdialog.setAlwaysOnTop(false);
        listKitJdialog.setVisible(true);
    }

    private void exibirPdfAnexo() {
        File arquivo = new File(addKitJdialog.getKitAnexoPanel().getFile().getAbsolutePath());
        try {
            Desktop.getDesktop().open(arquivo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAddModal(Kit kit)  {

        addKitJdialog.getFormPanel().clearForm();
        addKitJdialog.setSize(new Dimension(700,400));
        addKitJdialog.setPreferredSize(new Dimension(700,400));
        addKitJdialog.getFormPanel().setPreferredSize(new Dimension(700,400));

        if (kit instanceof Kit) {

            addKitJdialog.getFormPanel().setEntityOnForm(kit);

            if (!Strings.isNullOrEmpty(kit.getAnexo().trim())) {
                addKitJdialog.setSize(new Dimension(1100,400));
                addKitJdialog.setPreferredSize(new Dimension(1100,400));
                addKitJdialog.getFormPanel().setPreferredSize(new Dimension(700,400));
                addKitJdialog.getKitAnexoPanel().exibirAnexo(new File(kit.getAnexo()));
                addKitJdialog.getKitAnexoPanel().setVisible(true);
            } else {
                addKitJdialog.getKitAnexoPanel().setVisible(false);
            }

        } else {

            int proximoId = addKitJdialog.getFormPanel().getNextId();

            addKitJdialog.getFormPanel().setCodBarrasValue();
            addKitJdialog.getFormPanel().getIdTxtField().setText(String.valueOf(proximoId));
            addKitJdialog.getKitAnexoPanel().setVisible(false);
        }

        addKitJdialog.setLocationRelativeTo(null);
        addKitJdialog.setAlwaysOnTop(true);
        addKitJdialog.setVisible(true);
    }

    private void saveEntity() {

        KitFormPanel formPanel = addKitJdialog.getFormPanel();
        if (formPanel.validateForm()) {

            Kit kit = formPanel.getEntityFromForm();

            if (!Strings.isNullOrEmpty(kit.getAnexo().trim())) {
                try {
                    uploadFile(kit);
                } catch (Exception e) {
                    Notifications.showErrorMessage(e.getMessage());
                    return;
                }
            }
            kitService.save(kit);
            closeAddKitJdialog();

            String searchText = listKitJdialog.getListKitJpanel().getJtfFilter().getText();
            prepareAndOpenFrame();
            listKitJdialog.getListKitJpanel().getJtfFilter().setText(searchText);
        }
    }

    private void uploadFile(Kit kit) throws Exception {

        // Arquivo do form
        File arquivoOrigem = new File(kit.getAnexo());

        // Verifica se o arquivo existe
        Path pathOrigem = Paths.get(arquivoOrigem.getAbsolutePath());
        if (!Files.exists(pathOrigem)) {
            throw new FileNotFoundException(ConstMessages.Messages.ARQUIVO_NAO_ENCONTRADO);
        }

        // Valida mime type do arquivo
        String[] allowedFileMimeTypes = {"image/jpeg","image/png","application/pdf"};
        Tika tika = new Tika();
        String mimeType = null;

        try {
            mimeType = tika.detect(arquivoOrigem);
        } catch (IOException e) {
            throw new IOException(ConstMessages.Messages.TIPO_ARQUIVO_NAO_IDENTIFICADO);
        }

        boolean contains = Arrays.asList(allowedFileMimeTypes).contains(mimeType);
        if (!contains) {
            throw new InvalidFormatException(ConstMessages.Messages.FORMATO_ARQUIVO_INVALIDO);
        }

        // Pasta destino
        String pastaDestino = "C:\\etiquetas\\anexos\\" + kit.getId() + "\\";
        Path pathDestino = Paths.get(pastaDestino);
        if (!Files.exists(pathDestino)) {
            try {
                Files.createDirectories(pathDestino);
            } catch (IOException e) {
                throw new IOException(ConstMessages.Messages.ERRO_ESCRITA_DIRETORIO);
            }
        }

        // Arquivo destino
        File destino = new File(pastaDestino + arquivoOrigem.getName());

        // Verifica se o usuário mandou um arquivo diferente do antigo
        if (!arquivoOrigem.getAbsolutePath().equals(destino.getAbsolutePath())) {
            // Deleta arquivos da pasta destino.
            File outputDir = new File(pastaDestino);
            if (outputDir.isDirectory()) {
                File[] allFiles = outputDir.listFiles();
                for (File toDelete : allFiles) {
                    toDelete.delete();
                }
            }

            // Copia o arquivo de origem para o destino
            try {
                Files.copy(arquivoOrigem.toPath(),destino.toPath());
                Files.setLastModifiedTime(destino.toPath(), FileTime.fromMillis(new Date().getTime()));
            } catch (IOException e) {
                throw new IOException(ConstMessages.Messages.ERRO_ESCRITA_DIRETORIO);
            }

            // Atualiza o kit com o novo caminho do arquivo
            kit.setAnexo(destino.getAbsolutePath());
        }
    }

    private void closeAddKitJdialog() {
        addKitJdialog.getFormPanel().clearForm();
        addKitJdialog.dispose();
    }

    private void removeEntity() {

        int selectedRow = listKitJdialog.getListKitJpanel().getJTable().getSelectedRow();
        if(selectedRow < 0) {
            Notifications.showNonRowSelected();
        } else {
            Object[] options = { "Sim", "Não" };
            int confirm = JOptionPane.showOptionDialog(
                    null,
                    "Deseja excluir o Kit selecionado?",
                    "Excluir",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (confirm == JOptionPane.YES_OPTION) {

                //int id = (int) listKitJdialog.getListKitJpanel().getJTable().getModel().getValueAt(selectedRow, 0);

                int row = listKitJdialog.getListKitJpanel().getJTable().getSelectedRow();
                javax.swing.table.TableModel tableModel = listKitJdialog.getListKitJpanel().getJTable().getModel();
                JTable jTable = listKitJdialog.getListKitJpanel().getJTable();
                String id = tableModel.getValueAt(jTable.convertRowIndexToModel(row), 0).toString();

                Kit kit = kitService.findById(Integer.parseInt(id));

                // Deleta arquivos da pasta destino.
                File outputDir = new File("C:\\etiquetas\\anexos\\" + kit.getId());
                if (outputDir.isDirectory()) {
                    File[] allFiles = outputDir.listFiles();
                    for (File toDelete : allFiles) {
                        toDelete.delete();
                    }
                    outputDir.delete();
                }

                kitService.remove(kit);

                listKitJdialog.getListKitJpanel().getModel().removeRow(selectedRow);
                String searchText = listKitJdialog.getListKitJpanel().getJtfFilter().getText();
                prepareAndOpenFrame();
                listKitJdialog.getListKitJpanel().getJtfFilter().setText(searchText);
            }
        }
    }

    private void editEntity() {

        int selectedRow = listKitJdialog.getListKitJpanel().getJTable().getSelectedRow();
        if(selectedRow < 0) {
            Notifications.showNonRowSelected();
        } else {
            int id = (int) listKitJdialog.getListKitJpanel().getJTable().getModel().getValueAt(listKitJdialog.getListKitJpanel().getJTable().convertRowIndexToModel(selectedRow), 0);
            Kit kit = kitService.findById(id);
            showAddModal(kit);
        }
    }

}
