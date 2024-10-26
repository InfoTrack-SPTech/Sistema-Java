package conexao.banco;

import log.datas.GerarLog;
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

    static Conexao provedorBd = new Conexao();
    static JdbcTemplate connection = provedorBd.getConexaoDoBanco();

    public static void cadastrarBairrosBd(List<String> bairros, Connection conexao) throws IOException, SQLException {

        new GerarLog("cadastrarBairrosBd", "Iniciando inserção dos registros");
        conexao.setAutoCommit(true);

        String instrucaoSql = "INSERT INTO bairro(nome) VALUES";
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
    }

    public static void cadastrarLocaisBd(List<String> locais, Connection conexao) throws IOException, SQLException {

        new GerarLog("cadastrarLocaisBd", "Iniciando inserção dos registros");
        conexao.setAutoCommit(true);

        String instrucaoSql = "INSERT INTO local(nome) VALUES";
        for (int i = 0; i < locais.size(); i++){

            String local = locais.get(i).replace("\"", "").replace("\\", "").replace("=", "");
            if((i + 1) == locais.size()){
                instrucaoSql += """
                    \n (\"%s\"); """.formatted(local);
            } else{
                instrucaoSql += """
                    \n (\"%s\"), """.formatted(local);
            }
        }

        connection.execute(instrucaoSql);
        new GerarLog("cadastrarLocaisBd", "Finalizando inserção dos registros");
    }

    // Funcionalidades de consulta
    public static List<Bairro> consultarBairros(){

        List<Bairro> bairro = connection.query("SELECT * FROM bairro",
                    new BeanPropertyRowMapper<>(Bairro.class));

        return bairro;
    }

    public static List<Local> consultarLocais(){

        List<Local> local = connection.query("SELECT * FROM local",
                new BeanPropertyRowMapper<>(Local.class));

        return local;
    }

    public static List<Logradouro> consultarLogradouros(){

        List<Logradouro> logradouros = connection.query("SELECT * FROM logradouro",
                new BeanPropertyRowMapper<>(Logradouro.class));

        return logradouros;
    }
}
