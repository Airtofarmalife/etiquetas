package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.kit.KitRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.ExtensionFileFilter;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.validation.JTextFieldLimit;
import com.google.common.base.Strings;

import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Component
@Getter
public class KitFormPanel extends JPanel {

    private JTextField idTxtField;
    private JTextField descricao;
    private JTextField codBarras;
    private JTextField quantidade;
    private JComboBox tamanho;
    private JComboBox tiposEnvolucro;
    private JTextField envolucro;
    private JTextField anexo;
    private JComboBox quantidadeEnvolucro;
    private int id;
    private Kit kit = null;

    @PostConstruct
    private void preparePanel() {
        setPanelUp();
        initComponents();
    }

    @Autowired
    private KitRepository kitRepository;

    private void setPanelUp() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Informações do kit"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new MigLayout("gap 9 9,insets 5 5 5 5"));
    }


    public int getNextId() {
        return kitRepository.getNextValMySequence();
    }

    public void setCodBarrasValue() {
        this.codBarras.setText("1234" + getNextId());
    }

    private void initComponents() {

        JLabel idLbl = new JLabel(ConstMessages.Labels.KIT_ID);
        idTxtField = new JTextField(10);
        idTxtField.setDocument(new JTextFieldLimit(10));
        idTxtField.setText(String.valueOf(getNextId()));
        idTxtField.setEditable(false);
        idTxtField.setBackground(Color.LIGHT_GRAY);

        JLabel descricaoLbl = new JLabel(ConstMessages.Labels.KIT_DESCRICAO);
        descricao = new JTextField(22);
        descricao.setDocument(new JTextFieldLimit(30));

        JLabel codBarrasLbl = new JLabel(ConstMessages.Labels.KIT_COD_BARRAS);
        codBarras = new JTextField(10);
        setCodBarrasValue();
        codBarras.setDocument(new JTextFieldLimit(13));
        codBarras.setBackground(Color.LIGHT_GRAY);
        codBarras.setEditable(false);

        JLabel quantidadeLbl = new JLabel(ConstMessages.Labels.KIT_QUANTIDADE);
        quantidade = new JTextField(5);
        quantidade.setDocument(new JTextFieldLimit(6));

        JLabel tamanhoLbl = new JLabel(ConstMessages.Labels.KIT_TAMANHO);
        tamanho = new JComboBox();
        tamanho.addItem("P");
        tamanho.addItem("M");
        tamanho.addItem("G");
        tamanho.setEditable(true);

        JLabel envolucroLbl = new JLabel(ConstMessages.Labels.KIT_ENVOLUCRO);

        tiposEnvolucro = new JComboBox();
        tiposEnvolucro.addItem("SMS");
        tiposEnvolucro.addItem("PGRAU");
        tiposEnvolucro.setEditable(false);

        envolucro = new JTextField(7);
        envolucro.setDocument(new JTextFieldLimit(8));


        JLabel envolucroQuantidadeLbl = new JLabel(ConstMessages.Labels.KIT_TOTAL_ENVOLUCRO);
        quantidadeEnvolucro = new JComboBox();
        quantidadeEnvolucro.addItem("1");
        quantidadeEnvolucro.addItem("2");

        JLabel anexoLbl = new JLabel(ConstMessages.Labels.KIT_ANEXO);
        anexo =  new JTextField(30);
        anexo.setEditable(false);

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new ExtensionFileFilter(
                new String[] { ".PDF", ".PNG", "JPG",".JPEG" },
                "Extensões permitidas (.PDF | .PNG | .JPG | .JPEG)"));

        JButton upload = new JButton("Selecionar");
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.showOpenDialog(getParent());
                File arquivo = chooser.getSelectedFile();
                if(arquivo != null){
                    anexo.setText(arquivo.getAbsolutePath());
                }
            }
        });

        add(idLbl,"align label");
        add(idTxtField,"wrap");
        add(descricaoLbl,"align label");
        add(descricao,"wrap");
        add(codBarrasLbl,"align label");
        add(codBarras,"wrap");
        add(quantidadeLbl,"align label");
        add(quantidade,"wrap");
        add(tamanhoLbl,"align label");
        add(tamanho,"wrap");
        add(envolucroLbl,"align label");
        add(tiposEnvolucro,"split 4");
        add(envolucro);
        add(envolucroQuantidadeLbl,"align label");
        add(quantidadeEnvolucro,"wrap");
        add(anexoLbl,"align label");
        add(anexo,"split 2");
        add(upload,"wrap");

        this.revalidate();
    }

    public Kit getEntityFromForm() {

        if (this.kit == null) {
            this.kit = new Kit();
        }

        kit.setId(Integer.parseInt(idTxtField.getText()));
        kit.setDescricao(descricao.getText());
        kit.setCodbarras(codBarras.getText());
        kit.setQuantidade(Integer.parseInt(quantidade.getText()));
        kit.setTamanho(tamanho.getSelectedItem().toString());
        kit.setEnvolucro(envolucro.getText());
        kit.setTotalEnvolucro(Integer.parseInt(quantidadeEnvolucro.getSelectedItem().toString()));
        kit.setAnexo(anexo.getText());
        kit.setTipoEnvolucro(tiposEnvolucro.getSelectedItem().toString());
        return kit;
    }

    public void setEntityOnForm(Kit kit) {

        idTxtField.setText(kit.getId().toString());
        descricao.setText(kit.getDescricao());
        codBarras.setText(kit.getCodbarras());
        quantidade.setText(kit.getQuantidade().toString());
        tamanho.setSelectedItem(kit.getTamanho());
        envolucro.setText(kit.getEnvolucro());
        quantidadeEnvolucro.setSelectedItem(kit.getTotalEnvolucro().toString());
        anexo.setText(kit.getAnexo());
    }

    public boolean validateForm() {

        if(Strings.isNullOrEmpty(descricao.getText().trim()) ||
                descricao.getText().trim().length() > 30){
            Notifications.showAlertMessage("O nome deve conter entre 1 e 30 caracteres");
            return false;
        }

        if(Strings.isNullOrEmpty(codBarras.getText().trim())) {
            Notifications.showAlertMessage("Código de barras não informado");
            return false;
        } else {
            try {
                Integer.parseInt(codBarras.getText().trim());
            } catch (NumberFormatException exception) {
                Notifications.showAlertMessage("O Código de barras deve ser um valor inteiro");
                return false;
            }
        }

        if(Strings.isNullOrEmpty(quantidade.getText().trim())) {
            Notifications.showAlertMessage("Quantidade  não informada");
            return false;
        } else {
            try {
                if (Integer.parseInt(quantidade.getText().trim()) <= 0) {
                    Notifications.showAlertMessage("Quantidade deve ser um valor positivo");
                    return false;
                }
            } catch (NumberFormatException exception) {
                Notifications.showAlertMessage("Quantidade deve ser um valor numérico");
                return false;
            }
        }
/*
        if (Strings.isNullOrEmpty(envolucro.getText().trim()) ||
                envolucro.getText().length() > 30) {
            Notifications.showAlertMessage("O valor do envólucro não foi informado");
            return false;
        }

        if (Strings.isNullOrEmpty(tamanho.getSelectedItem().toString().trim())) {
            Notifications.showAlertMessage("O tamanho não foi informado");
            return false;
        }
*/
//        if (Strings.isNullOrEmpty(anexo.getText().trim())) {
//            Notifications.showAlertMessage("O anexo não foi selecionado");
//            return false;
//        }


        return true;
    }

    public void clearForm() {
        descricao.setText("");
        codBarras.setText("");
        quantidade.setText("");
        tamanho.setSelectedItem("");
        envolucro.setText("");
        anexo.setText("");
    }

}
