package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoKitsPorTurno {

    private String nomeKit;
    private int idKit;
    private String tamanho;
    private int totalManha = 0;
    private int totalTarde = 0;
    private int totalNoite = 0;

    public void incrementManha() {
        totalManha++;
    }
    public void incrementTarde() {
        totalTarde++;
    }
    public void incrementNoite() {
        totalNoite++;
    }

    public int getPorte() {
        return totalManha + totalTarde + totalNoite;
    }

}
