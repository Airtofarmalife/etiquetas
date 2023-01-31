package br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.periodo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Periodo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank
        private String nome;

        @NotBlank
        private LocalTime horaInicial;

        @NotBlank
        private LocalTime horaFinal;
}
