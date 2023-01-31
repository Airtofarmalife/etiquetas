package br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.historico.dto;

import com.google.common.base.Strings;
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
public class HistoricoKitsPorPeriodoWrapper {

    private int total = 0;

    private List<HistoricoKitsPorPeriodo> historicoKitsPorPeriodos;

    private Map<String,Integer> totalPorTamanho;

    public void setHistoricoKitsPorPeriodos(List<HistoricoKitsPorPeriodo> historicoKitsPorPeriodos) {
       this.historicoKitsPorPeriodos = historicoKitsPorPeriodos;
       setTotal();
       contabilizarPorTamanho();
    }

    private void setTotal() {
        this.historicoKitsPorPeriodos.forEach( (e) -> total += e.getQuantidade() );
    }

    private void contabilizarPorTamanho() {

        Map<String,Integer> totalPorTamanhoMap = new HashMap<>();

        this.historicoKitsPorPeriodos.forEach( (e) -> {

            if (!Strings.isNullOrEmpty(e.getTamanho())) {

                if(!totalPorTamanhoMap.containsKey(e.getTamanho())){
                    totalPorTamanhoMap.put(e.getTamanho(), e.getQuantidade());
                } else {
                    Integer total = totalPorTamanhoMap.get(e.getTamanho());
                    total += e.getQuantidade();
                    totalPorTamanhoMap.put(e.getTamanho(),total);
                }
            }
        });

        this.totalPorTamanho = new TreeMap<>(totalPorTamanhoMap);
    }

}
