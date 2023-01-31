package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.domain;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.etiqueta.controller.EtiquetaController;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.util.Objects;

@Getter
@Setter
public class CustomJRViewer extends JRViewer {

    private JasperPrint jasperPrint;
    private JComboBox numberOfCopiesComboBox;
    private JComboBox printersComboBox;

    private EtiquetaController etiquetaController;

    public CustomJRViewer(JasperPrint jrPrint) {

        super(jrPrint);

        this.jasperPrint = jrPrint;

        // Get save button
        JButton saveBtn = (JButton) this.tlbToolBar.getComponent(0);

        // Get print button and remove default listeners
        JButton printBtn = (JButton) this.tlbToolBar.getComponent(1);
        for (ActionListener al : printBtn.getActionListeners()) {
            printBtn.removeActionListener(al);
        }
        // Adds custom event
        printBtn.addActionListener(evt -> printDocument());

        // Gets separation panel
        JPanel pnlSep01 = (JPanel) tlbToolBar.getComponent(3);

        // Get zoom buttons
        JButton zoomInBtn = (JButton) this.tlbToolBar.getComponent(14);
        JButton zoomOutBtn = (JButton) this.tlbToolBar.getComponent(15);
        JComboBox zoomCombobox = (JComboBox) this.tlbToolBar.getComponent(16);

        // ToolBar cleanup
        this.tlbToolBar.removeAll();

        // Creates "number of copies" field
        numberOfCopiesComboBox = new JComboBox();
        numberOfCopiesComboBox.setSize(new Dimension(57, 9));
        numberOfCopiesComboBox.setPreferredSize(new Dimension(57, 22));
        for (int i = 1; i <= 10; i++) {
            numberOfCopiesComboBox.addItem(i);
        }
        numberOfCopiesComboBox.setMaximumRowCount(numberOfCopiesComboBox.getItemCount());
        numberOfCopiesComboBox.setEditable(true);

        // Creates printer's ComboBox
        printersComboBox = new JComboBox();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            if (!services[i].getName().toUpperCase().contains("PDF") &&
                    !services[i].getName().toUpperCase().contains("XPS") &&
                    !services[i].getName().toUpperCase().contains("ONENOTE") &&
                    !services[i].getName().toUpperCase().contains("FAX"))
                printersComboBox.addItem(services[i].getName());
        }

        //Add print button to right end of tool bar
        this.tlbToolBar.add(saveBtn);
        this.tlbToolBar.add(printBtn);
        this.tlbToolBar.add(printersComboBox);
        this.tlbToolBar.add(numberOfCopiesComboBox);
        this.tlbToolBar.add(pnlSep01);
        this.tlbToolBar.add(zoomInBtn);
        this.tlbToolBar.add(zoomOutBtn);
        this.tlbToolBar.add(zoomCombobox);

        // Set only PDF as save format
        JRSaveContributor[] saveContributors = tlbToolBar.getSaveContributors();
        JRPdfSaveContributor jrPdfSaveContributor = null;
        for (int i = 0; i < saveContributors.length; i++) {
            if (saveContributors[i] instanceof JRPdfSaveContributor) {
                jrPdfSaveContributor = (JRPdfSaveContributor) saveContributors[i];
            }
            tlbToolBar.removeSaveContributor(saveContributors[i]);
        }
        tlbToolBar.addSaveContributor(jrPdfSaveContributor);
    }


    private boolean printDocument() {

        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        int totalCopies = Integer.parseInt(numberOfCopiesComboBox.getSelectedItem().toString());
        printRequestAttributeSet.add(new Copies(totalCopies));

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

        String selectedPrinterName = printersComboBox.getSelectedItem().toString();
        printServiceAttributeSet.add(new PrinterName(selectedPrinterName, null));

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setExporterInput(new SimpleExporterInput(this.jasperPrint));

        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);
        exporter.setConfiguration(configuration);

        try {
            exporter.exportReport();
        } catch (JRException e) {
            throw new RuntimeException(e);
        }


        for (int i = 1; i <= totalCopies; i++) {
            etiquetaController.saveEntity();
        }

        return true;
    }

}
