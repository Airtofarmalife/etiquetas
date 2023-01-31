package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.view;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.border.Borders;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.ui.LookAndFeelUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Getter
@Component
@NoArgsConstructor
public class MainMenuFrame extends JFrame {

    public void setUpFrame() {
        getRootPane().setBorder(Borders.createEmptyBorder());
        setTitle(ConstMessages.Labels.MAIN_MENU);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        LookAndFeelUtils.setWindowsLookAndFeel();
        setPreferredSize(new Dimension(800,500));
        add(new JLabel(new ImageIcon("C:\\etiquetas\\logo.jpg")));
    }

}
