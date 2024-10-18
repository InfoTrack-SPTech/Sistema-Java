package conexao.banco;

import log.datas.GerarLog;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import java.io.IOException;
import java.util.List;

public class RegistrarDados {

    static Conexao provedorBd = new Conexao();
    static JdbcTemplate connection = provedorBd.getConexaoDoBanco();

    public static void cadastrarBairrosBd(List<String> bairros) throws IOException {

        new GerarLog("cadastrarBairrosBd", "Iniciando inserção dos registros");

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
    public static void cadastrarLogradourosBd(List<Logradouro> ruas) throws IOException {

        new GerarLog("cadastrarLogradourosBd", "Iniciando inserção dos registros");

        String instrucaoSql = "INSERT INTO logradouro(nome, numero, latitude, longitude, fkBairro) VALUES";
        for(int i = 0; i < ruas.size(); i++){

            String nmRuaFormatada = ruas.get(i).getNome().replace("\"", "");
            if((i + 1) == ruas.size()){
                instrucaoSql += """
                        \n (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\"); """
                        .formatted(nmRuaFormatada, ruas.get(i).getNumero(), ruas.get(i).getLatitude(), ruas.get(i).getLongitude(), ruas.get(i).getFkBairro());
            } else{
                instrucaoSql += """
                        \n (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\"), """
                        .formatted(nmRuaFormatada, ruas.get(i).getNumero(), ruas.get(i).getLatitude(), ruas.get(i).getLongitude(), ruas.get(i).getFkBairro());
            }
        }

        connection.execute(instrucaoSql);
        new GerarLog("cadastrarLogradourosBd", "Finalizando inserção dos registros");
    }

    // Funcionalidades de consulta
    public static Bairro consultarBairroPorNome(String nomeBairro){

        System.out.println(nomeBairro.replace("\"", "").replace("\\", "").replace("=", ""));
        Bairro bairro = connection.queryForObject("SELECT * FROM bairro WHERE nome = ? ORDER BY nome LIMIT 1",
                new BeanPropertyRowMapper<>(Bairro.class), nomeBairro.replace("\"", "").replace("\\", "").replace("=", ""));

        return bairro;
    }
}
