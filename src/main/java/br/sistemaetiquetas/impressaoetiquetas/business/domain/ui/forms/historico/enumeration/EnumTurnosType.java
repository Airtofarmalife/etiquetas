package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.enumeration;


import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public enum EnumTurnosType {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private final String name;

    public static EnumTurnosType from(String val) {
        return Stream.of(EnumTurnosType.values())
                .filter(e -> e.name.equals(val))
                .findFirst()
                .orElse(null);
    }

}
