package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.controller;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.helper.LoggedUser;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.empresa.controller.EmpresaController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.controller.EtiquetaController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.controller.HistoricoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.importacao.controller.ImportacaoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.kit.controller.KitController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.periodo.controller.PeriodoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.ResponsavelEsterelizacaoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.ResponsavelPreparoController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.listeners.*;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.mainmenu.view.MainMenuFrame;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.shared.controller.AbstractFrameController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;

import javax.swing.*;

@Controller
@AllArgsConstructor
public class MainMenuController extends AbstractFrameController {

    @Getter
    private final MainMenuFrame mainMenuFrame;
    private final EmpresaController empresaController;
    private final ResponsavelEsterelizacaoController responsavelEsterelizacaoController;
    private final ResponsavelPreparoController responsavelPreparoController;
    private final EtiquetaController etiquetaController;
    private final KitController kitController;
    private final HistoricoController historicoController;
    private final PeriodoController periodoController;
    private final ImportacaoController importacaoController;

    public void prepareAndOpenFrame() {

        prepareMainMenuFrameListeners();
        mainMenuFrame.setUpFrame();
        mainMenuFrame.pack();
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    private void prepareMainMenuFrameListeners() {

        EnumUsuariosType usuariosType = EnumUsuariosType.from(LoggedUser.tipoUsuario);

        switch (usuariosType) {
            case ADMINISTRADOR:
                montarMenuAdmin();
                break;
            case GESTOR:
                montarMenuGestor();
                break;
            case PADRAO:
                montarMenuPadrao();
                break;
        }
    }

    private void montarMenuAdmin () {

        // Barra de menus
        JMenuBar menuBar = new JMenuBar();

        //Menu cadastros
        JMenu menuCadastros = new JMenu("Cadastros");

        // Submenu kit
        KitMenuListener listenerKit = new KitMenuListener(kitController);
        JMenuItem kits = new JMenuItem("Kits");
        kits.addActionListener(listenerKit);
        menuCadastros.add(kits);

        // Responsavel preparo
        ResponsavelPreparoMenuListener listenerResponsavelPreparo = new ResponsavelPreparoMenuListener(responsavelPreparoController);
        JMenuItem responsavelPreparo = new JMenuItem("Responsáveis Preparo");
        responsavelPreparo.addActionListener(listenerResponsavelPreparo);
        menuCadastros.add(responsavelPreparo);

        // Responsavel esterelizacao
        ResponsavelEsterelizacaoMenuListener listenerResponsavelEsterelizacao = new ResponsavelEsterelizacaoMenuListener(responsavelEsterelizacaoController);
        JMenuItem responsavelEsterelizacao = new JMenuItem("Responsáveis Esterelização");
        responsavelEsterelizacao.addActionListener(listenerResponsavelEsterelizacao);
        menuCadastros.add(responsavelEsterelizacao);

        // Empresas
        EmpresaMenuListener listenerEmpresa = new EmpresaMenuListener(empresaController);
        JMenuItem empresas = new JMenuItem("Empresas");
        empresas.addActionListener(listenerEmpresa);
        menuCadastros.add(empresas);

        // Periodos
        PeriodoMenuListener periodoMenuListener = new PeriodoMenuListener(periodoController);
        JMenuItem periodos = new JMenuItem("Períodos");
        periodos.addActionListener(periodoMenuListener);
        menuCadastros.add(periodos);

        menuBar.add(menuCadastros);

        //Menu etiquetas
        JMenu menuEtiquetas = new JMenu("Etiquetas");

        // Geracao de etiquetas
        EtiquetaListener listenerEtiqueta = new EtiquetaListener(etiquetaController);
        JMenuItem etiquetas = new JMenuItem("Geração de etiqueta");
        etiquetas.addActionListener(listenerEtiqueta);
        menuEtiquetas.add(etiquetas);

        menuBar.add(menuEtiquetas);

        //Menu histórico
        JMenu menuHistorico = new JMenu("Histórico de kits");

        HistoricoListener listenerHistorico = new HistoricoListener(historicoController);
        JMenuItem historico = new JMenuItem("Gerar relatório");
        historico.addActionListener(listenerHistorico);
        menuHistorico.add(historico);

        menuBar.add(menuHistorico);

        // Menu importação
        JMenu menuImportacao = new JMenu("Importação");

        ImportacaoListener listenerImportacao = new ImportacaoListener(importacaoController);
        JMenuItem importacao = new JMenuItem("Cadastro em massa");
        importacao.addActionListener(listenerImportacao);
        menuImportacao.add(importacao);

        menuBar.add(menuImportacao);

        mainMenuFrame.setJMenuBar(menuBar);

        JMenu sair = new JMenu("Importação");
        JMenuItem fechar = new JMenuItem("Cadastro em massa");
        JMenuItem trocarUsuario = new JMenuItem("Cadastro em massa");


    }

    private void montarMenuGestor() {

        // Barra de menus
        JMenuBar menuBar = new JMenuBar();

        //Menu cadastros
        JMenu menuCadastros = new JMenu("Cadastros");

        // Submenu kit
        KitMenuListener listenerKit = new KitMenuListener(kitController);
        JMenuItem kits = new JMenuItem("Kits");
        kits.addActionListener(listenerKit);
        menuCadastros.add(kits);

        // Responsavel preparo
        ResponsavelPreparoMenuListener listenerResponsavelPreparo = new ResponsavelPreparoMenuListener(responsavelPreparoController);
        JMenuItem responsavelPreparo = new JMenuItem("Responsáveis Preparo");
        responsavelPreparo.addActionListener(listenerResponsavelPreparo);
        menuCadastros.add(responsavelPreparo);

        // Responsavel esterelizacao
        ResponsavelEsterelizacaoMenuListener listenerResponsavelEsterelizacao = new ResponsavelEsterelizacaoMenuListener(responsavelEsterelizacaoController);
        JMenuItem responsavelEsterelizacao = new JMenuItem("Responsáveis Esterelização");
        responsavelEsterelizacao.addActionListener(listenerResponsavelEsterelizacao);
        menuCadastros.add(responsavelEsterelizacao);

        menuBar.add(menuCadastros);

        //Menu etiquetas
        JMenu menuEtiquetas = new JMenu("Etiquetas");

        // Geracao de etiquetas
        EtiquetaListener listenerEtiqueta = new EtiquetaListener(etiquetaController);
        JMenuItem etiquetas = new JMenuItem("Geração de etiqueta");
        etiquetas.addActionListener(listenerEtiqueta);
        menuEtiquetas.add(etiquetas);

        menuBar.add(menuEtiquetas);

        //Menu histórico
        JMenu menuHistorico = new JMenu("Histórico de kits");

        HistoricoListener listenerHistorico = new HistoricoListener(historicoController);
        JMenuItem historico = new JMenuItem("Gerar relatório");
        historico.addActionListener(listenerHistorico);
        menuHistorico.add(historico);

        menuBar.add(menuHistorico);


        mainMenuFrame.setJMenuBar(menuBar);

    }

    private void montarMenuPadrao() {

        // Barra de menus
        JMenuBar menuBar = new JMenuBar();

        //Menu cadastros
        JMenu menuCadastros = new JMenu("Cadastros");

        // Responsavel preparo
        ResponsavelPreparoMenuListener listenerResponsavelPreparo = new ResponsavelPreparoMenuListener(responsavelPreparoController);
        JMenuItem responsavelPreparo = new JMenuItem("Responsáveis Preparo");
        responsavelPreparo.addActionListener(listenerResponsavelPreparo);
        menuCadastros.add(responsavelPreparo);

        menuBar.add(menuCadastros);

        //Menu etiquetas
        JMenu menuEtiquetas = new JMenu("Etiquetas");

        // Geracao de etiquetas
        EtiquetaListener listenerEtiqueta = new EtiquetaListener(etiquetaController);
        JMenuItem etiquetas = new JMenuItem("Geração de etiqueta");
        etiquetas.addActionListener(listenerEtiqueta);
        menuEtiquetas.add(etiquetas);

        menuBar.add(menuEtiquetas);

        //Menu histórico
        JMenu menuHistorico = new JMenu("Histórico de kits");

        HistoricoListener listenerHistorico = new HistoricoListener(historicoController);
        JMenuItem historico = new JMenuItem("Gerar relatório");
        historico.addActionListener(listenerHistorico);
        menuHistorico.add(historico);

        menuBar.add(menuHistorico);

        mainMenuFrame.setJMenuBar(menuBar);

    }

}
