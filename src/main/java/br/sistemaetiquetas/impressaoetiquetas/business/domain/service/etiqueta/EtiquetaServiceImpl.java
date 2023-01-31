package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.etiqueta;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta.Etiqueta;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.etiqueta.EtiquetaRepository;
import lombok.AllArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    @Override
    public List<Etiqueta> findAll() {
        return  etiquetaRepository.findAll();
    }

    @Override
    public void remove(Etiqueta etiqueta) { etiquetaRepository.delete(etiqueta);}

    @Override
    public void save(Etiqueta etiqueta) { etiquetaRepository.save(etiqueta);}


    @Override
    public List<Etiqueta> findByInterval(LocalDateTime dateTimeInicial, LocalDateTime dateTimeFinal) {
        return etiquetaRepository.findByInterval(dateTimeInicial, dateTimeFinal);
    }

    @Override
    public List<Etiqueta> findReportData(LocalDate diaInicial, LocalDate diaFinal, String turno) {
        return etiquetaRepository.findReportData(diaInicial, diaFinal,turno);
    }

    @Override
    public List<Etiqueta> findByIntervalAndEnvolucros(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros) {
        return etiquetaRepository.findByIntervalAndEnvolucros(localDateInicial, localDateFinal, envolucros);
    }

    @Override
    public List<Etiqueta> findByIntervalAndEnvolucrosWithType(LocalDate localDateInicial, LocalDate localDateFinal, List<String> envolucros, String tipoEnvolucro) {
        return etiquetaRepository.findByIntervalAndEnvolucrosWithType(localDateInicial, localDateFinal, envolucros,tipoEnvolucro);
    }

    @Override
    public List<String> findDistinctByEnvolucro() {
        return etiquetaRepository.findDistinctByEnvolucro();
    }

    @Override
    public List<String> findDistinctByEnvolucroSelecionado(String envolucro) {
        return etiquetaRepository.findDistinctByEnvolucroSelecionado(envolucro);
    }

    @Override
    public String generateEAN13BarcodeImageBase64(String barcodeText) throws Exception {

        Barcode barcode = BarcodeFactory.createCodabar(barcodeText);
        Font plainFont = new Font("Serif", Font.PLAIN, 24);
        barcode.setFont(plainFont);
        //System.out.println(barcode.getHeight());
        barcode.setBarHeight(32);
        BufferedImage img = BarcodeImageHandler.getImage(barcode);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(img, "JPG", b64os);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return os.toString();
    }

}
