package conexao.banco;

import log.datas.GerarLog;
import log.datas.S3Logs;
import org.apache.commons.dbcp2.Utils;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import utils.ManipularDadosUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ManipularDados {

    static RegistrarDados Bd = new RegistrarDados();
    static ManipularDadosUtils conversor = new ManipularDadosUtils();
    static Connection ctx;

    static {
        try {
            ctx = new Conexao().getConexao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ManipularDados() throws IOException {
    }

    public static void extrairBairros(List<List<Object>> planilha) throws IOException, SQLException {

        System.out.println("Iniciando extração dos Bairros");
        new GerarLog("extrairBairros", "Iniciando extração das informações");

        List<String> Bairros = new ArrayList<>();
        for (int item = 1; item <= planilha.size(); item++) {

            // 11 -> coluna onde possui os valores das celulas dos bairros
            String bairro = conversor.validarValorTexto(planilha.get(item).get(11));
            Boolean jaExtraido = Bairros.stream().anyMatch(x -> x.equalsIgnoreCase(bairro));
            if (!jaExtraido && bairro.length() > 0) {
                Bairros.add(bairro);
            }
            if(item % 50000 == 0){
                System.out.println("Quantidade lida: " + item);
                new GerarLog("extrairBairros", "Inserindo 50 mil registros...");
            }
        }

        new GerarLog("extrairBairros", "Finalizando extração das informações");
        S3Logs.subirArquivoBucket("extrairBairros");
        Bd.cadastrarBairrosBd(Bairros, ctx);
    }

    public static void extrairLogradouro(List<List<Object>> planilha) throws IOException, SQLException {

        System.out.println("Iniciando extração dos Logradouros");
        new GerarLog("extrairLogradouro", "Iniciando extração das informações");
        ctx.setAutoCommit(false);

        List<Bairro> bairros = Bd.consultarBairros();
        List<Logradouro> ruasJaCadastradas = Bd.consultarLogradouros();

        List<String> logradouros = new ArrayList<>();
        ruasJaCadastradas.forEach(r -> logradouros.add(r.getNome().toUpperCase() + "-" + r.getNumero()));
        HashSet<String> lista= new HashSet<>(logradouros);

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Logradouro (nome, numero, latitude, longitude, fkBairro) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);

        for(int i = 1; i <= planilha.size(); i++){

            // 12 -> coluna com o valor do endereço
            String endereco = conversor.validarValorTexto(planilha.get(i).get(12));
            // 13 -> coluna com o valor do numero do endereço
            String numero = conversor.validarValorNumerico(planilha.get(i).get(13));

            String chaveRua = endereco.toUpperCase() + "-" + numero;
            if(!lista.contains(chaveRua)) {

                String nomeBairro = conversor.validarValorTexto(planilha.get(i).get(11));
                Integer idBairro = conversor.validarConsultaBairroPorNome(bairros, nomeBairro);

                // 14 -> coluna com o valor latitude
                String latitude = conversor.validarValorNumerico(planilha.get(i).get(14));
                // 15 -> coluna com o valor longitude
                String longitude = conversor.validarValorNumerico(planilha.get(i).get(15));

                lista.add(chaveRua);

                prepararLote.setString(1, endereco);
                prepararLote.setString(2, numero);
                prepararLote.setString(3, latitude);
                prepararLote.setString(4, longitude);

                if (idBairro == 0) {
                    prepararLote.setNull(5, java.sql.Types.INTEGER);
                } else {
                    prepararLote.setInt(5, idBairro);
                }
                prepararLote.addBatch();

                // inseri a cada 5 mil registros
                if (i % 5000 == 0) {
                    prepararLote.executeBatch();
                    ctx.commit();
                }
                if(i % 50000 == 0){
                    System.out.println("Quantidade lida: " + i);
                    new GerarLog("extrairLogradouro", "Inserindo 50 mil registros...");
                }
            }
        }

        // salva o restante dos dados
        prepararLote.executeBatch();
        ctx.commit();
        new GerarLog("extrairLogradouro", "Finalizando extração das informações");
        S3Logs.subirArquivoBucket("extrairLogradouro");
    }

    public static void extrairLocais(List<List<Object>> planilha) throws IOException, SQLException {

        System.out.println("Iniciando extração dos Locais");
        new GerarLog("extrairLocais", "Iniciando extração das informações");

        List<String> locais = new ArrayList<>();
        List<Local> locaisJaCadastrados = Bd.consultarLocais();

        // Define os tipos de estabelecimentos permitidos
        Set<String> tiposNaoPermitidos = new HashSet<>(Arrays.asList(
                "Via pública", "Veiculo", "Acampamento", "Acostamento", "Balsa", "Área Comum", "Convento", "Aterro Sanitário",
                "Arena/Rodeio", "Aplicativos de Mensagem", "Aplicativos de Relacionamento", "Corredor", "Escada", "Escado Rolante",
                "Facebook", "Whatsapp", "Instagram", "Lago/Lagoa", "Mar Territorial", "Margem Direita de Rio", "Margem Esquerda de Rio",
                "Praia/Balneário", "Pela Internet", "Rodovia/Estrada", "Outros", "NULL"
        ));
        for (int item = 1; item <= planilha.size(); item++) {

            // 10 -> coluna onde possui os valores das celulas dos locais
            String local = conversor.validarValorTexto(planilha.get(item).get(10));

            // Verifica se o tipo de local está na lista de permitidos
            Boolean jaExisteLocal = locaisJaCadastrados.stream().anyMatch(x -> x.getNome().equalsIgnoreCase(local));
            if(!jaExisteLocal){
                if(!locais.contains(local) && !tiposNaoPermitidos.contains(local)){
                    locais.add(local);
                }
            }
            if(item % 50000 == 0){
                System.out.println("Quantidade lida: " + item);
                new GerarLog("extrairLocais", "Inserindo 50 mil registros...");
            }
        }
        new GerarLog("extrairLocais", "Finalizando extração das informações");
        S3Logs.subirArquivoBucket("extrairLocais");
        Bd.cadastrarLocaisBd(locais, ctx);
    }

    public static void extrairCrimes(List<List<Object>> planilha) throws IOException, SQLException, ParseException {

        System.out.println("Iniciando extração dos Crimes");
        new GerarLog("extrairCrimes", "Iniciando extração das informações");
        ctx.setAutoCommit(false);

        // Faz uma consulta no banco para pegar as informacoes cadastradas
        List<Logradouro> logradouros = Bd.consultarLogradouros();
        List<Local> locais = Bd.consultarLocais();

        List<String> tiposPermitidos = new ArrayList<>(List.of("FURTO", "ROUBO", "Furto", "Roubo", "Furto de coisa comum"));

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Crime(natureza, dataOcorrencia, descricao, fkLogradouro, fkLocal) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);

        for (int i = 1; i <= planilha.size(); i++) {

            // 22 -> coluna com o tipo de crime
            String natureza = conversor.validarValorTexto(planilha.get(i).get(22));

            // 20 -> coluna com a descrição do crime
            String descricao = conversor.validarValorTexto(planilha.get(i).get(20));

            // Verifique se a natureza e a descrição estão nos tipos permitidos
            if (tiposPermitidos.contains(natureza) || tiposPermitidos.contains(descricao)) {

                // 7 -> coluna com a data da ocorrência
                String dataOcorrencia = conversor.tranformarPadraoDataAnoMesDia(planilha.get(i).get(7), planilha.get(i).get(8));

                // 13 -> coluna com o valor do numero do endereço
                String numero = conversor.validarValorNumerico(planilha.get(i).get(13));

                String nomeLogradouro = conversor.validarValorTexto(planilha.get(i).get(12));
                Integer idLogradouro = conversor.validarConsultaLogradouroPorEnderecoENumero(logradouros, nomeLogradouro, numero);

                String local = conversor.validarValorTexto(planilha.get(i).get(10));
                Integer idLocal = conversor.validarConsultaLocalPorNome(locais, local);

                prepararLote.setString(1, natureza);
                prepararLote.setString(2, dataOcorrencia);
                prepararLote.setString(3, descricao);
                if (idLogradouro == 0) {
                    prepararLote.setNull(4, java.sql.Types.INTEGER);
                } else {
                    prepararLote.setInt(4, idLogradouro);
                }
                if (idLocal == 0) {
                    prepararLote.setNull(5, java.sql.Types.INTEGER);
                } else {
                    prepararLote.setInt(5, idLocal);
                }
                prepararLote.addBatch();

                // inseri a cada 5000 mil registros
                if(i % 5000 == 0){
                    prepararLote.executeBatch();
                    ctx.commit();
                }
                if(i % 50000 == 0){
                    System.out.println("Quantidade lida " + i);
                    new GerarLog("extrairCrimes", "Inserindo 50 mil registros...");
                }
            }
        }

        // salva o restante das informacoes
        prepararLote.executeBatch();
        ctx.commit();

        new GerarLog("extrairCrimes", "Finalizando extração das informações");
        S3Logs.subirArquivoBucket("extrairCrimes");
    }
}
