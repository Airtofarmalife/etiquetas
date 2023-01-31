package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.ui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;

import java.awt.*;

public class LookAndFeelUtils {

    public static void setWindowsLookAndFeel() {
        try {
            /*
            GraphicsEnvironment a=GraphicsEnvironment.getLocalGraphicsEnvironment();
             String[] fonts = a.getAvailableFontFamilyNames();
             for (int i=0; i < fonts.length;i++) {
                System.out.println(fonts[i]);
            }*/
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            FontUIResource myFont = new FontUIResource(new Font("Verdana", Font.PLAIN, 15));

            UIManager.put("CheckBoxMenuItem.acceleratorFont", myFont);
            UIManager.put("Button.font", myFont);
            UIManager.put("ToggleButton.font", myFont);
            UIManager.put("RadioButton.font", myFont);
            UIManager.put("CheckBox.font", myFont);
            UIManager.put("ColorChooser.font", myFont);
            UIManager.put("ComboBox.font", myFont);
            UIManager.put("Label.font", myFont);
            UIManager.put("List.font", myFont);
            UIManager.put("MenuBar.font", myFont);
            UIManager.put("Menu.acceleratorFont", myFont);
            UIManager.put("RadioButtonMenuItem.acceleratorFont", myFont);
            UIManager.put("MenuItem.acceleratorFont", myFont);
            UIManager.put("MenuItem.font", myFont);
            UIManager.put("RadioButtonMenuItem.font", myFont);
            UIManager.put("CheckBoxMenuItem.font", myFont);
            UIManager.put("OptionPane.buttonFont", myFont);
            UIManager.put("OptionPane.messageFont", myFont);
            UIManager.put("Menu.font", myFont);
            UIManager.put("PopupMenu.font", myFont);
            UIManager.put("OptionPane.font", myFont);
            UIManager.put("Panel.font", myFont);
            UIManager.put("ProgressBar.font", myFont);
            UIManager.put("ScrollPane.font", myFont);
            UIManager.put("Viewport.font", myFont);
            UIManager.put("TabbedPane.font", myFont);
            UIManager.put("Slider.font", myFont);
            UIManager.put("Table.font", myFont);
            UIManager.put("TableHeader.font", myFont);
            UIManager.put("TextField.font", myFont);
            UIManager.put("Spinner.font", myFont);
            UIManager.put("PasswordField.font", myFont);
            UIManager.put("TextArea.font", myFont);
            UIManager.put("TextPane.font", myFont);
            UIManager.put("EditorPane.font", myFont);
            UIManager.put("TabbedPane.smallFont", myFont);
            UIManager.put("TitledBorder.font", myFont);
            UIManager.put("ToolBar.font", myFont);
            UIManager.put("ToolTip.font", myFont);
            UIManager.put("Tree.font", myFont);
            UIManager.put("FormattedTextField.font", myFont);
            UIManager.put("IconButton.font", myFont);
            UIManager.put("InternalFrame.optionDialogTitleFont", myFont);
            UIManager.put("InternalFrame.paletteTitleFont", myFont);
            UIManager.put("InternalFrame.titleFont", myFont);



        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    ConstMessages.Messages.WINDOWS_STYLE_LOADING_ERROR_MESSAGE + e,
                    ConstMessages.Messages.TITULO_ALERTA,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}