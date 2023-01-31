package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa.EmpresaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.addEmpresa.AddEmpresaJDialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.addEmpresa.EmpresaFormBtnPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.addEmpresa.EmpresaFormPanel;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.search.ListEmpresaJdialog;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.view.search.ListEmpresaJpanel;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;


@Controller
@AllArgsConstructor
public class EmpresaController extends AbstractFrameController {

    private final AddEmpresaJDialog addEmpresaJDialog;
    private final EmpresaService empresaService;
    private ListEmpresaJdialog listEmpresaJdialog;

    @PostConstruct
    private void prepareListeners() {

        // Tela listagem
        ListEmpresaJpanel listEmpresaJpanel = listEmpresaJdialog.getListEmpresaJpanel();
        registerAction(listEmpresaJpanel.getEdiBtn(),(e -> editEntity()));
        registerAction(listEmpresaJpanel.getAddBtn(),(e -> showAddModal(null)));
        registerAction(listEmpresaJpanel.getRemoveBtn(),(e -> removeEntity()));

        EmpresaFormBtnPanel formBtnPanel = addEmpresaJDialog.getFormBtnPanel();
        registerAction(formBtnPanel.getSaveBtn(), (e) -> saveEntity());
        registerAction(formBtnPanel.getCancelBtn(), (e) -> closeAddEmpresaJdialog());
    }

    @Override
    public void prepareAndOpenFrame() {
        listEmpresaJdialog.setAlwaysOnTop(false);
        listEmpresaJdialog.setVisible(true);
    }

    private void showAddModal(Empresa empresa) {

        if (empresa != null) {
            addEmpresaJDialog.getEmpresaFormPanel().setEntityOnForm(empresa);
        } else {
            addEmpresaJDialog.getEmpresaFormPanel().clearForm();
            addEmpresaJDialog.getEmpresaFormPanel().initComponents();
        }

        addEmpresaJDialog.setSize(new Dimension(300,190));
        addEmpresaJDialog.setPreferredSize(new Dimension(300,190));
        addEmpresaJDialog.setLocationRelativeTo(null);
        addEmpresaJDialog.pack();
        addEmpresaJDialog.setAlwaysOnTop(true);
        addEmpresaJDialog.setVisible(true);
    }

    private void closeAddEmpresaJdialog() {
        addEmpresaJDialog.getEmpresaFormPanel().clearForm();
        addEmpresaJDialog.dispose();
    }


    private void saveEntity() {

        EmpresaFormPanel empresaFormPanel = addEmpresaJDialog.getEmpresaFormPanel();

        if (empresaFormPanel.validateForm()) {
            Empresa empresa = empresaFormPanel.getEntityFromForm();
            empresaService.save(empresa);
            closeAddEmpresaJdialog();
            listEmpresaJdialog.setVisible(true);
        }
    }


    private void editEntity() {

        int selectedRow = listEmpresaJdialog.getListEmpresaJpanel().getJTable().getSelectedRow();
        if(selectedRow < 0) {
            Notifications.showNonRowSelected();
        } else {
            int id = (int) listEmpresaJdialog.getListEmpresaJpanel().getJTable().getModel().getValueAt(selectedRow, 0);
            Empresa empresa = empresaService.findById(id);
            showAddModal(empresa);
        }
    }

    private void removeEntity() {

        int selectedRow = listEmpresaJdialog.getListEmpresaJpanel().getJTable().getSelectedRow();
        if(selectedRow < 0) {
            Notifications.showNonRowSelected();
        } else {
            Object[] options = { "Sim", "Não" };
            int confirm = JOptionPane.showOptionDialog(
                    null,
                    "Deseja excluir a empresa selecionada?",
                    "Excluir",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) listEmpresaJdialog.getListEmpresaJpanel().getJTable().getModel().getValueAt(selectedRow, 0);
                Empresa empresa = empresaService.findById(id);
                empresaService.remove(empresa);
                listEmpresaJdialog.getListEmpresaJpanel().getModel().removeRow(selectedRow);
            }
        }
    }

}
