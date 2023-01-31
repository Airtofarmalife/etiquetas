package br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kit {

        @Id
        private Integer id;

        @NotBlank
        private String descricao;

        @NotBlank
        private String codbarras;

        @NotBlank
        private Integer quantidade;

        @NotBlank
        private String tamanho;

        @NotBlank
        private String envolucro;

        private String anexo;

        @NotBlank
        private Integer totalEnvolucro;

        @NotBlank
        private String tipoEnvolucro;
}
