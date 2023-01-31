package br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario;


import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique=true)
    private String nome;

    @NotBlank
    private String nomeComplementar;

    private String senha;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name="tipo")
    private EnumUsuariosType usuariosType;



    public String getUsuariosType() {
        return usuariosType.toString();
    }

}
