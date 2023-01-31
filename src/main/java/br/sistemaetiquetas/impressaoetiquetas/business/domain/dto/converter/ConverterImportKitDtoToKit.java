package br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.converter;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.kit.KitImportDto;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;

public final class ConverterImportKitDtoToKit {

    private ConverterImportKitDtoToKit(){}

    public static Kit convertDtoToKit(KitImportDto dto) {

        Kit kit = new Kit();
        kit.setId(dto.getId());
        kit.setDescricao(dto.getDescricao());
        kit.setCodbarras(dto.getCodigoBarras());
        kit.setQuantidade(dto.getQuantidade());
        kit.setTamanho(dto.getTamanho());
        kit.setEnvolucro(dto.getEnvolucro());
        kit.setTotalEnvolucro(dto.getTotalEnvolucro());
        kit.setTipoEnvolucro(dto.getTipoEnvolucro().toUpperCase());
        kit.setAnexo("");

        return kit;

    }

}
