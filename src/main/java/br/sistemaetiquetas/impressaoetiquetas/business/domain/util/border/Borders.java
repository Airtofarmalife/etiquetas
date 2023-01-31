package br.sistemaetiquetas.impressaoetiquetas.business.domain.util.border;

import javax.swing.*;
import javax.swing.border.Border;

public class Borders {

    private static final int WIDTH = 0;
    private static final int HEIGHT = 0;

    public static Border createEmptyBorder() {
        return BorderFactory.createEmptyBorder(HEIGHT, WIDTH, HEIGHT, WIDTH);
    }

}
