package conexao.banco;

import log.datas.GerarLog;
import log.datas.S3Logs;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RegistrarDados {

    static Conexao provedorBd;

    static {
        try {
            provedorBd = new Conexao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static JdbcTemplate connection = provedorBd.getConexaoDoBanco();

    public static void cadastrarBairrosBd(List<String> bairros, Connection conexao) throws IOException, SQLException {

        new GerarLog("cadastrarBairrosBd", "Iniciando inserção dos registros");
        conexao.setAutoCommit(true);

        String instrucaoSql = "INSERT INTO Bairro(nome) VALUES";
        for (int i = 0; i < bairros.size(); i++){

            String bairro = bairros.get(i).replace("\"", "").replace("\\", "").replace("=", "");
            if((i + 1) == bairros.size()){
                instrucaoSql += """
                    \n (\"%s\"); """.formatted(bairro);
            } else{
                instrucaoSql += """
                    \n (\"%s\"), """.formatted(bairro);
            }
        }

        connection.execute(instrucaoSql);
        new GerarLog("cadastrarBairrosBd", "Finalizando inserção dos registros");
        S3Logs.subirArquivoBucket("cadastrarBairrosBd");
    }

    public static void cadastrarLocaisBd(List<String> locais, Connection conexao) throws IOException, SQLException {

        new GerarLog("cadastrarLocaisBd", "Iniciando inserção dos registros");
        conexao.setAutoCommit(true);

        String instrucaoSql = "INSERT INTO Local(nome) VALUES";
        for (int i = 0; i < locais.size(); i++){

            String local = locais.get(i).replace("\"", "").replace("\\", "").replace("=", "");
            if((i + 1) == locais.size()){
                instrucaoSql += """
                    \n (\"%s\"); """.formatted(local);
                System.out.println(instrucaoSql);
            } else{
                instrucaoSql += """
                    \n (\"%s\"), """.formatted(local);
                System.out.println(instrucaoSql);

            }
        }

        connection.execute(instrucaoSql);
        new GerarLog("cadastrarLocaisBd", "Finalizando inserção dos registros");
        S3Logs.subirArquivoBucket("cadastrarLocaisBd");
    }

    // Funcionalidades de consulta
    public static List<Bairro> consultarBairros(){

        List<Bairro> bairro = connection.query("SELECT * FROM Bairro",
                    new BeanPropertyRowMapper<>(Bairro.class));

        return bairro;
    }

    public static List<Local> consultarLocais(){

        List<Local> local = connection.query("SELECT * FROM Local",
                new BeanPropertyRowMapper<>(Local.class));

        return local;
    }

    public static List<Logradouro> consultarLogradouros(){

        List<Logradouro> logradouros = connection.query("SELECT * FROM Logradouro",
                new BeanPropertyRowMapper<>(Logradouro.class));

        return logradouros;
    }
}
