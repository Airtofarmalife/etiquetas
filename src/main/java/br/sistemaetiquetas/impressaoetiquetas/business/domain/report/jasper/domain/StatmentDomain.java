package br.sistemaetiquetas.impressaoetiquetas.business.domain.report.jasper.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatmentDomain {

    private String image;
    private String description;
    private String value;
    private String date;
    private String textColor;

}
