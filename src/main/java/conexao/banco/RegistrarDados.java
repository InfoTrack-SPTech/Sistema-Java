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
