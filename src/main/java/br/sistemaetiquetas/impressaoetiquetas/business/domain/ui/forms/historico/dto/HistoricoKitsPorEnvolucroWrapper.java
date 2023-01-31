package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoricoKitsPorEnvolucroWrapper {

    private List<HistoricoKitsPorEnvolucro> historicoKitsPorEnvolucros;

    public TreeMap<String,Integer> contabilizarPorTamanho() {

        Map<String,Integer> totalPorTamanhoMap = new HashMap<>();

        historicoKitsPorEnvolucros.forEach( (e) -> e.getTotalPorTamanho().forEach( (tamanho, total) ->{

            if(!totalPorTamanhoMap.containsKey(tamanho)){
                totalPorTamanhoMap.put(tamanho,total);
            } else {
                Integer tamanhoAtual = totalPorTamanhoMap.get(tamanho);
                tamanhoAtual += total;
                totalPorTamanhoMap.put(tamanho,tamanhoAtual);
            }
        }));

        return new TreeMap<>(totalPorTamanhoMap);
    }


}
