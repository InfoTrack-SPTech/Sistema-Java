package conexao.banco;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {

    private JdbcTemplate conexaoDoBanco;
    private Connection conexao;
    
    public Conexao(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/InfoTrack");
        dataSource.setUsername("root");
        dataSource.setPassword("12345");

        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
    
    public Connection getConexao() throws SQLException {
        this.conexao = getConexaoDoBanco().getDataSource().getConnection();
        conexao.setAutoCommit(false);

        return conexao;
    }
    
    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
