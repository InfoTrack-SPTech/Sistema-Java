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

    public void cadastrarLogradourosBd(List<String> nomes, List<String> numeros, List<String> latitudes, List<String> longitudes) {

        for (String nome : nomes){
            connection.execute("INSERT INTO logradouro(nome) VALUES(\"%s\")".formatted(nome));
        }

        for (String numero : numeros){
            connection.execute("INSERT INTO logradouro(numero) VALUES(\"%s\")".formatted(numero));
        }

        for (String latitude : latitudes){
            connection.execute("INSERT INTO logradouro(latitude) VALUES(\"%s\")".formatted(latitude));
        }

        for (String longitude : longitudes){
            connection.execute("INSERT INTO logradouro(longitude) VALUES(\"%s\")".formatted(longitude));
        }
    }

    public static void cadastrarCrimesBd(List<String> crimes, List<String> datas, List<String> descricoes){

        for (String crime : crimes){
            connection.execute("INSERT INTO crime(natureza) VALUES(\"%s\")".formatted(crime));
        }

        for (String data : datas){
            connection.execute("INSERT INTO crime(dataOcorrencia) VALUES(\"%s\")".formatted(data));
        }

        for (String descricao : descricoes){
            connection.execute("INSERT INTO crime(descricao) VALUES(\"%s\")".formatted(descricao));
        }
    }

    public void cadastrarLocaisBd(List<String> locais) {

        for (String local : locais){
            connection.execute("INSERT INTO local(nome) VALUES(\"%s\")".formatted(local));
        }
    }
}
