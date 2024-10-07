package conexao.banco;
import org.springframework.jdbc.core.JdbcTemplate;

public class MainConexao {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.execute("DROP TABLE IF EXISTS zona");

        con.execute("CREATE TABLE Zona (\n" +
                "    idZona INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    nome VARCHAR(45)\n" +
                ");");

    }
}
