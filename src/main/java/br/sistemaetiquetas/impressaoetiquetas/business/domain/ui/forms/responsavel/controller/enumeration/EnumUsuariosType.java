package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration;


import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public enum EnumUsuariosType {
    PADRAO("PADRAO"),
    ADMINISTRADOR("ADMINISTRADOR"),
    GESTOR("GESTOR"),
    RESPONSAVEL_PREPARO("RESPONSAVEL_PREPARO"),
    RESPONSAVEL_ESTERELIZACAO("RESPONSAVEL_ESTERELIZACAO");

    private final String name;

    public static EnumUsuariosType from(String val) {
        return Stream.of(EnumUsuariosType.values())
                .filter(e -> e.name.equals(val))
                .findFirst()
                .orElse(null);
    }



}
