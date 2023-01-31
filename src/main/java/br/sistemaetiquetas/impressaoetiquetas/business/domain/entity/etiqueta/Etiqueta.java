package br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.etiqueta;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Etiqueta {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank
        private Integer idKit;

        @NotBlank
        private String empresa;

        @NotBlank
        private String nomeKit;

        @NotBlank
        private String codBarras;

        @NotBlank
        private String tipo;

        @NotBlank
        private String lote;

        @NotBlank
        private String ciclo;

        @NotBlank
        private LocalDate esterelizacao;

        @NotBlank
        private LocalDate validade;

        @NotBlank
        private String responsavelEsterelizacao;

        @NotNull
        private String temperatura;

        @NotNull
        private String temperaturaTipo;

        @NotNull
        private String temperaturaValor;

        @NotNull
        private String outras;

        @NotBlank
        private String responsavelPreparo;

        @NotBlank
        private Integer quantidade;

        @NotBlank
        private String observacao;

        @CreationTimestamp
        private Timestamp horarioGeracao;

        @NotBlank
        private Integer totalEnvolucro;

        @NotBlank
        private String envolucro;

        @NotBlank
        private String tipoEnvolucro;

        private String tamanho;

}
