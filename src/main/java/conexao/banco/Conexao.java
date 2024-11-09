package conexao.banco;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private JdbcTemplate conexaoDoBanco;
    private Connection conexao;
    
    public Conexao() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/InfoTrack");
        dataSource.setUsername("root");
        dataSource.setPassword("12345");

        conexaoDoBanco = new JdbcTemplate(dataSource);
        criarEstruturaBanco(conexaoDoBanco);
    }
    
    public Connection getConexao() throws SQLException {
        this.conexao = getConexaoDoBanco().getDataSource().getConnection();
        conexao.setAutoCommit(false);

        return conexao;
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }

    public void criarEstruturaBanco(JdbcTemplate conectarBd) throws SQLException {

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Bairro (
                idBairro INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100)
            );    
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Logradouro (
                idLogradouro INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100),
                numero VARCHAR(10),
                latitude varchar(12),
                longitude varchar(12),
                fkBairro INT,
                FOREIGN KEY (fkBairro) REFERENCES Bairro(idBairro)
            );
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Local (
                idLocal INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100)
            );
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Crime (
                idCrime INT AUTO_INCREMENT PRIMARY KEY,
                natureza VARCHAR(100) ,
                dataOcorrencia DATETIME,
                descricao VARCHAR(255),     \s
                fkLogradouro INT,
                fkLocal INT,
                FOREIGN KEY (fkLogradouro) REFERENCES Logradouro(idLogradouro),
                FOREIGN KEY (fkLocal) REFERENCES Local(idLocal)
            );  
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Empresa (
                idEmpresa INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,\s
                cnpj CHAR(14) NOT NULL,
                telefone CHAR(15)
            );
        """);

        conectarBd.execute("""
            INSERT INTO Empresa (nome, cnpj, telefone)
                SELECT 'InfoTrack', '12345678000199', '11999999999'
                WHERE NOT EXISTS (
                    SELECT 1 FROM Empresa WHERE cnpj = '12345678000199'
            );
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Cargo (
                idCargo INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(50)
            );
        """);

        conectarBd.execute("""
            INSERT INTO Cargo (nome)
            SELECT "Analista"\s
            WHERE NOT EXISTS (SELECT 1 FROM Cargo WHERE nome = "Analista");
        """);

        conectarBd.execute("""
            INSERT INTO Cargo (nome)
            SELECT "Gerente"\s
            WHERE NOT EXISTS (SELECT 1 FROM Cargo WHERE nome = "Gerente");
        """);

        conectarBd.execute("""
            INSERT INTO Cargo (nome)
            SELECT "Administrador"\s
            WHERE NOT EXISTS (SELECT 1 FROM Cargo WHERE nome = "Administrador");
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Usuario (
                idUsuario INT,
                email VARCHAR(80) NOT NULL,
                nome VARCHAR(80) NOT NULL,
                senha VARCHAR(60) NOT NULL,\s
                telefone VARCHAR(15),       \s
                fkCargo INT NOT NULL,
                FOREIGN KEY (fkCargo) REFERENCES Cargo(idCargo),
                fkEmpresa INT NOT NULL,
                PRIMARY KEY (idUsuario, fkEmpresa),
                FOREIGN KEY (fkEmpresa) REFERENCES Empresa(idEmpresa)
            );
        """);

        String query = "SELECT EXTRA FROM information_schema.COLUMNS WHERE TABLE_NAME = 'Usuario' AND COLUMN_NAME = 'idUsuario'";
        String extra = conectarBd.queryForObject(query, String.class);
        if (!"auto_increment".equalsIgnoreCase(extra)) {
            conectarBd.execute("ALTER TABLE Usuario MODIFY COLUMN idUsuario INT AUTO_INCREMENT");
        }

        conectarBd.execute("""
            INSERT INTO Usuario (email, nome, senha, telefone, fkCargo, fkEmpresa)
            SELECT 'matheusFerro@infotrack.com', 'Matheus Ferro', 'Matheus10', '11999911111', 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'matheusFerro@infotrack.com');
        """);

        conectarBd.execute("""
            INSERT INTO Usuario (email, nome, senha, telefone, fkCargo, fkEmpresa)
            SELECT 'brunoGomes@infotrack.com', 'Bruno Gomes', 'Bruno20', '11999922222', 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'brunoGomes@infotrack.com');
        """);

        conectarBd.execute("""
            INSERT INTO Usuario (email, nome, senha, telefone, fkCargo, fkEmpresa)
            SELECT 'biancaRodrigues@infotrack.com', 'Bianca Rodrigues', 'Bianca30', '11999933333', 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'biancaRodrigues@infotrack.com');
        """);

        conectarBd.execute("""
            INSERT INTO Usuario (email, nome, senha, telefone, fkCargo, fkEmpresa)
            SELECT 'alejandroCastor@infotrack.com', 'Alejandro Castor', 'Alejandro40', '11999944444', 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'alejandroCastor@infotrack.com');
        """);

        conectarBd.execute("""
            INSERT INTO Usuario (email, nome, senha, telefone, fkCargo, fkEmpresa)
            SELECT 'cintiaOhara@infotrack.com', 'Cintia Ohara', 'Cintia50', '11999955555', 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'cintiaOhara@infotrack.com');
        """);

        conectarBd.execute("""
            CREATE TABLE IF NOT EXISTS Recomendacao (
                idRecomendacao INT PRIMARY KEY AUTO_INCREMENT,
                fkEmpresa INT,
                descricao TEXT,
                tipoRecomendacao VARCHAR(50),
                dataGeracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                geradoPor VARCHAR(50) DEFAULT 'IA',
                FOREIGN KEY (fkEmpresa) REFERENCES Empresa(idEmpresa)
            );
        """);
    }
}
