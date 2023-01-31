package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.view.addKit;

import lombok.Getter;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@Getter
public class KitAnexoPanel extends JPanel {

    private JLabel imagemPlaceholder;
    private JButton showPdfBtn;
    private File file;

    @PostConstruct
    private void initComponents() {

        imagemPlaceholder = new JLabel();
        showPdfBtn = new JButton("Exibir PDF");

        add(imagemPlaceholder);
        add(showPdfBtn);

        imagemPlaceholder.setVisible(false);
        showPdfBtn.setVisible(false);

    }

    public void exibirAnexo(File file) {

        this.file = file;

        Tika tika = new Tika();
        String mimeType = null;

        try {
            mimeType = tika.detect(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (mimeType.equals("image/jpeg") || mimeType.equals("image/png")) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image scaleImage = icon.getImage().getScaledInstance(400, 400,Image.SCALE_DEFAULT);
            imagemPlaceholder.setIcon(new ImageIcon(scaleImage));
            imagemPlaceholder.setVisible(true);
            showPdfBtn.setVisible(false);

        }

        if (mimeType.equals( "application/pdf")) {
            imagemPlaceholder.setVisible(false);
            showPdfBtn.setVisible(true);
        }

        revalidate();
        setVisible(true);
    }

}
