package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.kit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.converter.ConverterImportKitDtoToKit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.kit.KitImportDto;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.kit.KitRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant.ConstMessages;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@AllArgsConstructor
public class KitServiceImpl implements KitService {

    private final KitRepository kitRepository;

    @Override
    public List<Kit> findAll() {
        return kitRepository.findAll();
    }


    @Override
    public void remove(Kit kit) { kitRepository.delete(kit);}

    @Override
    public void save(Kit kit) {kitRepository.save(kit);}

    @Override
    public List<Kit> findAllByOrderByDescricao() {
        return kitRepository.findAllByOrderByDescricao();
    }

    @Override
    public boolean importar(File arquivo) {

        List<Kit> kitsImportados = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = criarWorkbookAPartirDoArquivo(arquivo);
        } catch (Exception e) {
            Notifications.showErrorMessage(e.getMessage());
            return false;
        }

        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();

        boolean deuErro = false;

        // Avança linhas
        while (rowIterator.hasNext() && !deuErro) {

            Row row = rowIterator.next();

            if (row.getRowNum() == 0) { continue;} // Ignora header

            if (!linhaEmBranco(row)) {
                try {

                    KitImportDto importDto = KitImportDto.builder()
                            .id((int) row.getCell(0).getNumericCellValue())
                            .descricao(row.getCell(1).getStringCellValue())
                            .codigoBarras(String.valueOf((int)row.getCell(2).getNumericCellValue()))
                            .quantidade((int)row.getCell(3).getNumericCellValue())
                            .tamanho(row.getCell(4).getStringCellValue())
                            .envolucro(String.valueOf((int)row.getCell(5).getNumericCellValue()))
                            .totalEnvolucro((int) row.getCell(6).getNumericCellValue())
                            .tipoEnvolucro(row.getCell(7).getStringCellValue())
                            .build();

                    if(validarDtoImport(importDto,row)) {
                        kitsImportados.add(ConverterImportKitDtoToKit.convertDtoToKit(importDto));
                    } else {
                        deuErro = true;
                        kitsImportados = new ArrayList<>();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(deuErro) {
            return false;
        }

        if (kitsImportados.size() > 0) {
            kitsImportados.forEach(kitRepository::save);
        }

        return true;
    }

    private boolean linhaEmBranco(Row row) {
        if (row.getFirstCellNum() > 0) {
            for (int cellNum = row.getFirstCellNum(); cellNum <= 7; cellNum++) {
                Cell cell = row.getCell(cellNum);
                if (cell == null || cell.getCellType() == CellType.BLANK || Strings.isNullOrEmpty((cell.toString()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Workbook criarWorkbookAPartirDoArquivo(File arquivo) throws Exception {

        validarAquivo(arquivo);
        InputStream inputStream = new FileInputStream(arquivo);

        Workbook workbook = null;

        if(arquivo.getName().toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if(arquivo.getName().toLowerCase().endsWith("xls")) {
            workbook =  new HSSFWorkbook(inputStream);
        }

        return workbook;
    }


    private boolean validarDtoImport(KitImportDto dto,Row row) throws IllegalArgumentException {

        String mensagem = "Erro na linha " + row.getRowNum() + ": ";

        if (dto.getId() <= 0) {
            Notifications.showErrorMessage(mensagem + "O ID deve ser um número postitivo");
            return false;
        }
        if (Strings.isNullOrEmpty(dto.getDescricao()) || dto.getDescricao().length() > 30) {
            Notifications.showErrorMessage(mensagem + "A descricão conter entre 1 e 30 caracteres");
            return false;
        }
        if (Strings.isNullOrEmpty(dto.getCodigoBarras())) {
            Notifications.showErrorMessage(mensagem + "Nenhum código de barras informado");
            return false;
        }
        if (dto.getTotalEnvolucro() <= 0) {
            Notifications.showErrorMessage(mensagem + "Total do envólucro deve ser um número positivo");
            return false;
        }
        if (!Objects.equals(dto.getTipoEnvolucro().toUpperCase(),"PGRAU") &&
                !Objects.equals(dto.getTipoEnvolucro().toUpperCase(),"SMS") ) {
            Notifications.showErrorMessage(mensagem + "O tipo de envólucro deve ser PGRAU ou SMS");
            return false;
        }
        return true;
    }

    private void validarAquivo(File arquivoExcel) throws Exception {

        // Verifica se o arquivo existe
        Path pathOrigem = Paths.get(arquivoExcel.getAbsolutePath());
        if (!Files.exists(pathOrigem)) {
            throw new FileNotFoundException(ConstMessages.Messages.ARQUIVO_NAO_ENCONTRADO);
        }

        // Detecta o mime type
        Tika tika = new Tika();
        String mimeType = null;
        try {
            mimeType = tika.detect(arquivoExcel);
        } catch (IOException e) {
            throw new Exception(ConstMessages.Messages.TIPO_ARQUIVO_NAO_IDENTIFICADO);
        }

        String[] allowedFileMimeTypes = {
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        };

        // Valida se a extesão é permitida
        boolean contains = Arrays.asList(allowedFileMimeTypes).contains(mimeType);
        if (!contains) {
            throw new Exception(ConstMessages.Messages.FORMATO_ARQUIVO_INVALIDO);
        }
    }

    public Kit findById(Integer id) { return kitRepository.findById(id).orElse(null); }

}
