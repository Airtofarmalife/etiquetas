package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoKitsPorPeriodo {

    private int idKit;
    private String nomeKit;
    private String lote;
    private String ciclo;
    private String temperaturaValor;
    private String responsavel;
    private String tamanho;
    private int quantidade = 0;

    public void incrementQuantidade() {
        quantidade++;
    }

}
