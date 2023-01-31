package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo.Periodo;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.periodo.PeriodoService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.model.PeriodoTableModel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.PeriodoTableBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.PeriodoTableFrame;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.modal.AddPeriodoDialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.modal.PeriodoFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.view.modal.PeriodoFormPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.List;

@Controller
@AllArgsConstructor
public class PeriodoController extends AbstractFrameController {

    private final PeriodoTableFrame tableFrame;
    private final AddPeriodoDialog addFrame;
    private final PeriodoTableModel tableModel;
    private final PeriodoService periodoService;

    @PostConstruct
    private void prepareListeners() {
        PeriodoTableBtnPanel tableBtnPanel = tableFrame.getTableBtnPanel();
        PeriodoFormBtnPanel formBtnPanel = addFrame.getFormBtnPanel();

        registerAction(tableBtnPanel.getAddBtn(), (e) -> showAddModal());
        registerAction(tableBtnPanel.getRemoveBtn(), (e) -> removeEntity());
        registerAction(formBtnPanel.getSaveBtn(), (e) -> saveEntity());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeModalWindow());
    }

    @Override
    public void prepareAndOpenFrame() {
        loadEntities();
        showTableFrame();
    }

    private void loadEntities() {
        List<Periodo> entities = periodoService.findAll();
        tableModel.clear();
        tableModel.addEntities(entities);
    }

    private void showTableFrame() {
        tableFrame.setVisible(true);
    }

    private void showAddModal() {
        addFrame.setVisible(true);
    }

    private void saveEntity() {

        PeriodoFormPanel formPanel = addFrame.getFormPanel();
        if (formPanel.validateForm()) {
            Periodo entity = formPanel.getEntityFromForm();
            periodoService.save(entity);
            tableModel.addEntity(entity);
            closeModalWindow();
        }
    }

    private void closeModalWindow() {
        addFrame.getFormPanel().clearForm();
        addFrame.dispose();
    }

    private void removeEntity() {
        try {
            JTable clientTable = tableFrame.getTablePanel().getTable();
            int selectedRow = clientTable.getSelectedRow();
            if (selectedRow < 0) {
               Notifications.showNonRowSelected();
            } else {

                Object[] options = { "Sim", "Não" };
                int confirm = JOptionPane.showOptionDialog(
                        null,
                        "Deseja excluir o período selecionado?",
                        "Excluir",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (confirm == JOptionPane.YES_OPTION) {

                    Periodo entity = tableModel.getEntityByRow(selectedRow);
                    periodoService.remove(entity);
                    tableModel.removeRow(selectedRow);
                }
            }
        } catch (Exception e) {
            Notifications.showDeleteRowErrorMessage();
        }
    }

}
