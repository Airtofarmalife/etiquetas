package br.sistemaetiquetas.impressaoetiquetas;

//import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.jasper.PdfService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.login.controller.LoginController;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.ui.LookAndFeelUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@SpringBootApplication
public class ImpressaoEtiquetasApp {

	public static void main(String[] args) {
		createDirs();
		LookAndFeelUtils.setWindowsLookAndFeel();
		ConfigurableApplicationContext context = createApplicationContext(args);
		displayMainFrame(context);
	}

	private static void createDirs() {
		new File("C:\\etiquetas").mkdirs();
		new File("C:\\etiquetas\\temp").mkdirs();
		new File("C:\\etiquetas\\jasper\\etiqueta").mkdirs();
		new File("C:\\etiquetas\\jasper\\relatorioEnvolucros").mkdirs();
		new File("C:\\etiquetas\\jasper\\relatorioLivre").mkdirs();
		new File("C:\\etiquetas\\jasper\\relatorioTurnos").mkdirs();

		copyResources("/jasper/etiqueta/etiqueta.jrxml","C:\\etiquetas\\jasper\\etiqueta\\etiqueta.jrxml");
		copyResources("/jasper/relatorioLivre/relatorio_livre.jrxml","C:\\etiquetas\\jasper\\relatorioLivre\\relatorio_livre.jrxml");
		copyResources("/jasper/relatorioLivre/subreport_livre.jrxml","C:\\etiquetas\\jasper\\relatorioLivre\\subreport_livre.jrxml");
		copyResources("/jasper/relatorioEnvolucros/envolucros.jrxml","C:\\etiquetas\\jasper\\relatorioEnvolucros\\envolucros.jrxml");
		copyResources("/jasper/relatorioEnvolucros/statements_envolucro_pgrau.jrxml","C:\\etiquetas\\jasper\\relatorioEnvolucros\\statements_envolucro_pgrau.jrxml");
		copyResources("/jasper/relatorioEnvolucros/statements_envolucro_tnt.jrxml","C:\\etiquetas\\jasper\\relatorioEnvolucros\\statements_envolucro_tnt.jrxml");
		copyResources("/jasper/relatorioTurnos/full_statement.jrxml","C:\\etiquetas\\jasper\\relatorioTurnos\\full_statement.jrxml");
		copyResources("/jasper/relatorioTurnos/statements.jrxml","C:\\etiquetas\\jasper\\relatorioTurnos\\statements.jrxml");
		copyResources("/image/logo.jpg","C:\\etiquetas\\logo.jpg");
	}

	private static void copyResources(String inputFile , String destFile) {

		URL inputUrl = ImpressaoEtiquetasApp.class.getResource(inputFile);
		File dest = new File(destFile);
		try {
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private static ConfigurableApplicationContext createApplicationContext(String[] args) {
		return new SpringApplicationBuilder(ImpressaoEtiquetasApp.class)
				.headless(false)
				.run(args);
	}

	private static void displayMainFrame(ConfigurableApplicationContext context) {

		SwingUtilities.invokeLater(() -> {
			LoginController loginController = context.getBean(LoginController.class);
			loginController.prepareAndOpenFrame();

		});
	}

}
