package br.sistemaetiquetas.impressaoetiquetas.business.domain.dto.login;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {
    private String usuario;
    private String senha;
}


