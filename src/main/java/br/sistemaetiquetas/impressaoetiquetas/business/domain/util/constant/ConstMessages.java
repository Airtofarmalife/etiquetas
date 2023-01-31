package br.sistemaetiquetas.impressaoetiquetas.business.domain.util.constant;

public interface ConstMessages {

    interface DialogTitles {

        String EMPRESA_MODAL = "Cadastro de empresa";
        String KIT_MODAL = "Cadastro de Kit";
        String USUARIO_MODAL = "Adicionar Usuario";
        String ETIQUETA_MODAL = "Geração de Etiqueta";
        String HISTORICO_MODAL = "Histórico de kits";
        String PERIODO_MODAL = "Cadastro de período";
        String IMPORTACAO = "Importar kits em massa";
        String LOGIN = "Login";
    }

    interface Messages {

        String WINDOWS_STYLE_LOADING_ERROR_MESSAGE = "Ocorreu um erro ao definir a aparência das janelas ";
        String TITULO_ALERTA = "Alerta";
        String NENHUM_REGISTRO_SELECIONADO = "Nenhum registro selecionado";
        String ERRO_AO_REMOVER_O_REGISTRO = "Erro ao remover o registro.";

        // Upload
        String FORMATO_ARQUIVO_INVALIDO = "Formato de arquivo inválido.";
        String ARQUIVO_NAO_ENCONTRADO =  "O arquivo selecionado não foi encontrado";
        String ERRO_ESCRITA_DIRETORIO = "Erro de escrita ao salvar o arquivo";
        String TIPO_ARQUIVO_NAO_IDENTIFICADO = "O tipo do arquivo não pode ser identificado";
    }

    interface Labels {

        String MAIN_MENU = "Sistema gerador de etiquetas";

        String ADD_BTN = "Adicionar";
        String BOTAO_CANCELAR = "Cancelar";
        String BOTAO_REMOVER = "Remover";
        String BOTAO_EDITAR = "Editar";
        String BOTAO_SALVAR = "Gravar";
        String BOTAO_IMPRIMIR = "Imprimir";
        String BOTAO_ENVIAR = "Enviar";
        String BOTAO_ENTRAR = "Entrar";


        String EMPRESA_NOME = "Nome";
        String EMPRESA_VALIDADE = "Validade";
        String EMPRESA_BUSCAR = "Buscar empresa";
        String KIT_ID = "ID";
        String KIT_DESCRICAO = "Descrição";
        String KIT_COD_BARRAS = "Codigo de barras";
        String KIT_QUANTIDADE = "Quantidade PC.";
        String KIT_TAMANHO = "Tamanho";
        String KIT_ENVOLUCRO = "Envólucro";
        String KIT_ANEXO = "Anexo";
        String KIT_TOTAL_ENVOLUCRO = "QTDE";

        String USUARIO_NOME = "Nome/Código";
        String USUARIO_NOME_COMPLEMENTAR = "Nome Complementar";
        String USUARIO_LOGIN_SENHA = "Senha";
        String USUARIOS_PREPARADOR = "Responsáveis Preparo";
        String USUARIOS_ESTERELIZADOR = "Responsáveis Esterelização";

        String ETIQUETA_EMPRESA = "Empresa";
        String ETIQUETA_CONSULTA = "Consulta";
        String ETIQUETA_CODBARRAS = "Cod. Barras";
        String ETIQUETA_NOME_KIT = "Nome";
        String ETIQUETA_TIPO = "Tipo";
        String ETIQUETA_LOTE = "Lote";
        String ETIQUETA_CICLO = "Ciclo";
        String ETIQUETA_TEMPERATURA = "Temperatura";
        String ETIQUETA_DATA_ESTERELIZACAO = "Esterelização";
        String ETIQUETA_DATA_VALIDADE = "Validade";
        String ETIQUETA_OUTRAS = "Outras";
        String ETIQUETA_RESPONSAVEL_PREPARO = "Res. Preparo";
        String ETIQUETA_RESPONSAVEL_ESTERELIZACAO = "Res. Esteril";
        String ETIQUETA_QUANTIDADE = "Pc";
        String ETIQUETA_OBSERVACAO = "Observação";
        String ETIQUETAS_PROCURAR_KIT = "Buscar kit";
        String ETIQUETAS_ENVOLUCRO_LABEL = "Filtrar Envólucros";

        String ETIQUETAS_SENHA_RESPONSAVEL = "Senha";

        String HISTORICO_EMPRESAS = "Empresa";
        String HISTORICO_TIPO = "Tipo";
        String HISTORICO_TIPO_ENVOLUCRO = "Tipo envólucro";
        String HISTORICO_RELATORIO = "Relatório";
        String HISTORICO_PERIODO_INICIAL = "Período Inicial";
        String HISTORICO_PERIODO_FINAL = "Período Final";
        String HISTORICO_BOTAO_IMPRESSAO = "Visualizar Impressão";
        String HISTORICO_RELATORIO_CANCELADO = "Relatório abortado";

        String PERIODOS = "Períodos";
        String PERIODO_NOME = "Turno";
        String PERIODO_HORA_INICIAL = "Hora Inicial";
        String PERIODO_HORA_FINAL = "Hora Final";

        String LOGIN_USUARIO = "Usuário";
        String LOGIN_SENHA = "Senha";

    }

}
