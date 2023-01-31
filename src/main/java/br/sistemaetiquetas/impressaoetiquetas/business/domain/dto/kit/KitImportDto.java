package br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.kit;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KitImportDto {

    private int id;
    private String descricao;
    private String codigoBarras;
    private int quantidade;
    private String tamanho;
    private String envolucro;
    private int totalEnvolucro;
    private String tipoEnvolucro;

}
