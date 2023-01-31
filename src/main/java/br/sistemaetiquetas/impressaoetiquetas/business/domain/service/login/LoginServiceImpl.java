package br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.empresa.Empresa;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.usuario.Usuario;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.usuario.UsuarioRepository;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.empresa.EmpresaService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.login.helper.LoggedUser;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.service.usuario.UsuarioService;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.ui.forms.responsavel.controller.enumeration.EnumUsuariosType;
import br.sistemaetiquetas.impressaoetiquetas.business.domain.util.notification.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @Autowired
    private EmpresaService empresaService;

    @Override
    public boolean login(String usuario, String senha) {

        Usuario usuarioResult = usuarioRepository.findByNome(usuario);

        if (Objects.isNull(usuarioResult)) {
            Notifications.showAlertMessage("Dados incorretos");
            return false;
        }


        boolean isAdmin = usuarioResult.getUsuariosType().equals(EnumUsuariosType.ADMINISTRADOR.toString());
        if (!isAdmin) {
            List<Empresa> empresas = empresaService.findAll();
            if (empresas.size() > 0) {
                LocalDate dataAtual = LocalDate.now();
                if ( empresas.get(0).getValidade().isBefore(dataAtual) ) {
                    Notifications.showAlertMessage("A validade do sistema expirou. Contate o administrador para realizar a renovação");
                    return false;
                }
            }
        }


        String hashedPassword = hashPassword(usuario,senha);
        if(!Objects.equals(hashedPassword,usuarioResult.getSenha())) {
            Notifications.showAlertMessage("Dados incorretos");
            return false;
        }

        LoggedUser.logado = true;
        LoggedUser.tipoUsuario = usuarioResult.getUsuariosType();

        return true;
    }


    public boolean verificarCredenciais(String usuario, String senha, String nomeComplementar) {

        Usuario usuarioResult = null;
       /* try {
            usuarioResult = usuarioService.findByNomeAndUsuariosType(usuario,EnumUsuariosType.RESPONSAVEL_ESTERELIZACAO);
        } catch (IncorrectResultSizeDataAccessException e) {
            Notifications.showAlertMessage("Este esterelizador foi cadastrado mais de uma vez. Por favor corrija a duplicidade no cadastro");
            return false;
        }*/

        usuarioResult = usuarioService.findByNomeAndUsuariosTypeAndNomeComplementar(
                usuario, EnumUsuariosType.RESPONSAVEL_ESTERELIZACAO, nomeComplementar);
        if (usuarioResult == null) {
            Notifications.showAlertMessage("Usuario não encontrado");
            return false;
        }

        // Concatena nome + nome complementar
        String nomeCompleto = usuario + usuarioResult.getNomeComplementar().trim();

        String hashedPassword = hashPassword(nomeCompleto,senha);
        if(!Objects.equals(hashedPassword,usuarioResult.getSenha())) {
            Notifications.showAlertMessage("Senha incorreta");
            return false;
        }

        return true;
    }


    @Override
    public void logoff() {

    }

    public List<Usuario> getLogins() {
        return usuarioRepository.getLogins();
    }

    public String hashPassword(String usuario, String senha) {

        byte[] saltBytes = usuario.getBytes(StandardCharsets.UTF_8);

        KeySpec spec = new PBEKeySpec(senha.toCharArray(), saltBytes, 65536, 128);
        SecretKeyFactory factory = null;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String base64encoded = "";
        try {

            byte[] hash = factory.generateSecret(spec).getEncoded();
            base64encoded = Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return base64encoded;
    }

}
