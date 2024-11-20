package conexao.banco;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class BancoDados {

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

    public static List<Crime> consultarCrimes(){

        List<Crime> crimes = connection.query("SELECT * FROM Crime",
                new BeanPropertyRowMapper<>(Crime.class));

        return crimes;
    }

}
