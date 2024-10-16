package conexao.banco;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RegistrarDados {

    static Conexao provedorBd = new Conexao();
    static JdbcTemplate connection = provedorBd.getConexaoDoBanco();

    public static void cadastrarBairrosBd(List<String> bairros){

        for (String bairro : bairros){
            connection.execute("INSERT INTO bairro(nome) VALUES(\"%s\")".formatted(bairro));
        }
    }
}
