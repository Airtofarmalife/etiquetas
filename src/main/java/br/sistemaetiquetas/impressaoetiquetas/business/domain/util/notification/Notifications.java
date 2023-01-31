package br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import javax.swing.*;

public class Notifications {

    public static void showAlertMessage(String message) {

        JOptionPane op = new JOptionPane(message,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = op.createDialog("Alerta");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

    }

    public static void showErrorMessage(String message) {

        JOptionPane op = new JOptionPane(message,JOptionPane.ERROR_MESSAGE);
        JDialog dialog = op.createDialog("Erro");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

    }

    public static void showDeleteRowErrorMessage() {
        JOptionPane.showMessageDialog(null,
                ConstMessages.Messages.ERRO_AO_REMOVER_O_REGISTRO,
                ConstMessages.Messages.TITULO_ALERTA,
                JOptionPane.ERROR_MESSAGE);
    }


    public static void showNonRowSelected() {
        JOptionPane.showMessageDialog(null,
                ConstMessages.Messages.NENHUM_REGISTRO_SELECIONADO,
                ConstMessages.Messages.TITULO_ALERTA,
                JOptionPane.ERROR_MESSAGE);
    }


}
