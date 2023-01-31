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
public class HistoricoKitsPorTurnoWrapper {

    private int totalManha = 0;
    private int totalTarde = 0;
    private int totalNoite = 0;
    public int getTotalTurnos() {
        return totalManha + totalTarde + totalNoite;
    }
    private void addManha(int valor) {
        totalManha+= valor;
    }
    private void addTarde(int valor) {
        totalTarde+= valor;
    }
    private void addNoite(int valor) {
        totalNoite += valor;
    }
    private List<HistoricoKitsPorTurno> historicoKitsPorTurnoList;
    private Map<String,Integer> totalPorTamanho;

    public void setHistoricoKitsPorTurnoList(List<HistoricoKitsPorTurno> historicoKitsPorTurnoList) {
        this.historicoKitsPorTurnoList = historicoKitsPorTurnoList;
        this.historicoKitsPorTurnoList.forEach( (e) -> {
                this.addManha(e.getTotalManha());
                this.addTarde(e.getTotalTarde());
                this.addNoite(e.getTotalNoite());
        });

        contabilizarPorTamanho();

    }

    private void contabilizarPorTamanho() {

        Map<String,Integer> totalPorTamanhoMap = new HashMap<>();

        this.historicoKitsPorTurnoList.forEach( (e) -> {

            if (!Strings.isNullOrEmpty(e.getTamanho())) {

                if(!totalPorTamanhoMap.containsKey(e.getTamanho())){
                    totalPorTamanhoMap.put(e.getTamanho(), e.getPorte());
                } else {
                    Integer total = totalPorTamanhoMap.get(e.getTamanho());
                    total += e.getPorte();
                    totalPorTamanhoMap.put(e.getTamanho(),total);
                }
            }
        });

        this.totalPorTamanho = new TreeMap<>(totalPorTamanhoMap);
    }


}
