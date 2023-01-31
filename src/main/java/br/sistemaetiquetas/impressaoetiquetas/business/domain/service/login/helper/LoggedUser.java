package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.helper;

import org.springframework.stereotype.Component;

@Component
public class LoggedUser {

    private LoggedUser(){}

    public static boolean logado = false;
    public static String tipoUsuario;
}
