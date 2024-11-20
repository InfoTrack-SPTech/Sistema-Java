package conexao.banco;

import log.datas.GerarLog;
import log.datas.S3Logs;
import log.datas.SlackNotificador;
import log.datas.SlackNotificador;
import utils.ManipularDadosUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class ManipularDados {

    static BancoDados Bd = new BancoDados();
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
        SlackNotificador.envioNotificacao("Atualizando base de dados dos Bairros...");
        ctx.setAutoCommit(false);

        List<Bairro> bairros = Bd.consultarBairros();
        HashSet<String> bairrosCadastrados = new HashSet<>();

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Bairro(nome) VALUES (?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);
        for (int item = 1; item < planilha.size(); item++) {

            // 11 -> coluna onde possui os valores das celulas dos bairros
            String bairro = conversor.validarValorTexto(planilha.get(item).get(11));
            Boolean jaExtraido = bairros.stream().anyMatch(x -> x.getNome().equalsIgnoreCase(bairro));
            if (!jaExtraido && !bairrosCadastrados.contains(bairro)) {
                bairrosCadastrados.add(bairro);

                prepararLote.setString(1, bairro);
                prepararLote.addBatch();
            }
            if(item % 5000 == 0){
                prepararLote.executeBatch();
                ctx.commit();
            }
            if(item % 50000 == 0){
                System.out.println("Quantidade lida: " + item);
                new GerarLog("extrairBairros", "Quantidade total lida: " + item);
            }
        }

        // salva o restante dos dados
        prepararLote.executeBatch();
        ctx.commit();

        SlackNotificador.envioNotificacao("Atualização da base dos Bairros finalizada...");
        new GerarLog("extrairBairros", "Finalizando extração das informações (Quantidade total lida: " + planilha.size() + ")");
        S3Logs.subirArquivoBucket("extrairBairros");
    }

    public static void extrairLogradouro(List<List<Object>> planilha) throws IOException, SQLException {

        System.out.println("Iniciando extração dos Logradouros");
        new GerarLog("extrairLogradouro", "Iniciando extração das informações");
        SlackNotificador.envioNotificacao("Atualizando base de dados dos Logradouros...");
        ctx.setAutoCommit(false);

        List<Bairro> bairros = Bd.consultarBairros();
        List<Logradouro> ruasJaCadastradas = Bd.consultarLogradouros();
        bairros = conversor.associarRuasAoBairro(ruasJaCadastradas, bairros);

        HashSet<String> lista= new HashSet<>();
        bairros.forEach(b -> b.getRuas().forEach(r -> lista.add(r.getNome().toUpperCase() + "-" + r.getNumero())));

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Logradouro (nome, numero, latitude, longitude, fkBairro) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);

        for(int i = 1; i < planilha.size(); i++){

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

            }
            // inseri a cada 5 mil registros
            if (i % 5000 == 0) {
                prepararLote.executeBatch();
                ctx.commit();
            }
            if(i % 50000 == 0){
                System.out.println("Quantidade lida: " + i);
                new GerarLog("extrairLogradouro", "Quantidade total lida: " + i);
            }
        }

        // salva o restante dos dados
        prepararLote.executeBatch();
        ctx.commit();

        SlackNotificador.envioNotificacao("Atualização da base dos Logradouros finalizada...");
        new GerarLog("extrairLogradouro", "Finalizando extração das informações (Quantidade total lida: " + planilha.size() + ")");
        S3Logs.subirArquivoBucket("extrairLogradouro");
    }

    public static void extrairLocais(List<List<Object>> planilha) throws IOException, SQLException {

        System.out.println("Iniciando extração dos Locais");
        new GerarLog("extrairLocais", "Iniciando extração das informações");
        SlackNotificador.envioNotificacao("Atualizando base de dados dos Locais...");
        ctx.setAutoCommit(false);

        List<Local> locaisJaCadastrados = Bd.consultarLocais();
        HashSet<String> locais = new HashSet<>();

        // Define os tipos de estabelecimentos permitidos
        Set<String> tiposNaoPermitidos = new HashSet<>(Arrays.asList(
                "Via pública", "Veiculo", "Acampamento", "Acostamento", "Balsa", "Área Comum", "Convento", "Aterro Sanitário",
                "Arena/Rodeio", "Aplicativos de Mensagem", "Aplicativos de Relacionamento", "Corredor", "Escada", "Escado Rolante",
                "Facebook", "Whatsapp", "Instagram", "Lago/Lagoa", "Mar Territorial", "Margem Direita de Rio", "Margem Esquerda de Rio",
                "Praia/Balneário", "Pela Internet", "Rodovia/Estrada", "Outros", "NULL"
        ));

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Local(nome) VALUES (?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);
        for (int item = 1; item < planilha.size(); item++) {

            // 10 -> coluna onde possui os valores das celulas dos locais
            String local = conversor.validarValorTexto(planilha.get(item).get(10));

            // Verifica se o tipo de local está na lista de permitidos
            Boolean jaExisteLocal = locaisJaCadastrados.stream().anyMatch(x -> x.getNome().equalsIgnoreCase(local));
            if(!jaExisteLocal){
                if(!locais.contains(local) && !tiposNaoPermitidos.contains(local)){
                    locais.add(local);

                    prepararLote.setString(1, local);
                    prepararLote.addBatch();
                }
            }
            if(item % 5000 == 0){
                prepararLote.executeBatch();
                ctx.commit();
            }
            if(item % 50000 == 0){
                System.out.println("Quantidade lida: " + item);
                new GerarLog("extrairLocais", "Quantidade total lida: " + item);
            }
        }

        // salva o restante dos dados
        prepararLote.executeBatch();
        ctx.commit();

        SlackNotificador.envioNotificacao("Atualização da base dos Locais finalizada...");
        new GerarLog("extrairLocais", "Finalizando extração das informações (Quantidade total lida: " + planilha.size() + ")");
        S3Logs.subirArquivoBucket("extrairLocais");
    }

    public static void extrairCrimes(List<List<Object>> planilha) throws IOException, SQLException, ParseException {

        System.out.println("Iniciando extração dos Crimes");
        new GerarLog("extrairCrimes", "Iniciando extração das informações");
        SlackNotificador.envioNotificacao("Atualizando base de dados dos Crimes...");
        ctx.setAutoCommit(false);

        // Faz uma consulta no banco para pegar as informacoes cadastradas
        List<Local> locais = Bd.consultarLocais();
        List<Logradouro> logradouros = Bd.consultarLogradouros();
        List<Crime> crimes = Bd.consultarCrimes();

        logradouros = conversor.associarCrimesAoLogradouro(logradouros, crimes);
        HashSet<String> tiposPermitidos = new HashSet<>(List.of("FURTO", "ROUBO", "Furto", "Roubo", "Furto de coisa comum"));

        // vai permitir a inserção em lotes
        String instrucaoSql = "INSERT INTO Crime(natureza, dataOcorrencia, artigo, fkLogradouro, fkLocal) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement prepararLote = ctx.prepareStatement(instrucaoSql);

        for (int i = 1; i < planilha.size(); i++) {

            // 22 -> coluna com o tipo de crime
            String natureza = conversor.validarValorTexto(planilha.get(i).get(22));

            // 20 -> coluna com a descrição do crime
            String artigo = conversor.validarValorTexto(planilha.get(i).get(21));

            // Verifique se a natureza e a descrição estão nos tipos permitidos
            if (tiposPermitidos.contains(natureza)) {

                // 7 -> coluna com a data da ocorrência
                String dataOcorrencia = conversor.tranformarPadraoDataAnoMesDia(planilha.get(i).get(7), planilha.get(i).get(8));

                // 13 -> coluna com o valor do numero do endereço
                String numero = conversor.validarValorNumerico(planilha.get(i).get(13));

                String nomeLogradouro = conversor.validarValorTexto(planilha.get(i).get(12));
                Integer idLogradouro = conversor.validarConsultaLogradouroPorEnderecoENumero(logradouros, nomeLogradouro, numero);

                String local = conversor.validarValorTexto(planilha.get(i).get(10));
                Integer idLocal = conversor.validarConsultaLocalPorNome(locais, local);

                if(natureza.toLowerCase().contains("furto")){

                    Furto furto = new Furto(0, dataOcorrencia, idLogradouro, idLocal, artigo);
                    prepararLote.setString(1, furto.getNatureza());
                    prepararLote.setString(2, furto.getDataOcorrencia());
                    prepararLote.setString(3, furto.getArtigo());
                    prepararLote.setObject(4, furto.getFkLogradouro() == 0 ? null : idLogradouro, java.sql.Types.INTEGER);
                    prepararLote.setObject(5, furto.getFkLocal() == 0 ? null : idLocal, java.sql.Types.INTEGER);
                } else{

                    Roubo roubo = new Roubo(0, dataOcorrencia, idLogradouro, idLocal, artigo);
                    prepararLote.setString(1, roubo.getNatureza());
                    prepararLote.setString(2, roubo.getDataOcorrencia());
                    prepararLote.setString(3, roubo.getArtigo());
                    prepararLote.setObject(4, roubo.getFkLogradouro() == 0 ? null : idLogradouro, java.sql.Types.INTEGER);
                    prepararLote.setObject(5, roubo.getFkLocal() == 0 ? null : idLocal, java.sql.Types.INTEGER);
                }
                prepararLote.addBatch();
            }
            // inseri a cada 5000 mil registros
            if(i % 5000 == 0){
                prepararLote.executeBatch();
                ctx.commit();
            }
            if(i % 50000 == 0){
                System.out.println("Quantidade lida " + i);
                new GerarLog("extrairCrimes", "Quantidade total lida: " + i);
            }
        }

        // salva o restante das informacoes
        prepararLote.executeBatch();
        ctx.commit();

        SlackNotificador.envioNotificacao("Atualização da base dos Crimes finalizada...");
        new GerarLog("extrairCrimes", "Finalizando extração das informações (Quantidade total lida: " + planilha.size() + ")");
        S3Logs.subirArquivoBucket("extrairCrimes");
    }
}
