package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto;

import com.google.common.base.Strings;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoKitsPorEnvolucro {

    private String envolucro;
    private int quantidade = 0;
    private int multiplicador;

    @Builder.Default
    private Map<String,Integer> totalPorTamanho = new HashMap<>();

    public void incrementQuantidade(String tamanho) {
        quantidade++;
        incrementTamanho(tamanho);
    }

    private void incrementTamanho(String tamanho) {

        tamanho = tamanho.trim();

        if (!Strings.isNullOrEmpty(tamanho)) {
            if (totalPorTamanho.containsKey(tamanho)) {
                Integer total = totalPorTamanho.get(tamanho);
                total++;
                totalPorTamanho.put(tamanho, total);
            } else {
                totalPorTamanho.put(tamanho, 1);
            }

        }
    }
}
