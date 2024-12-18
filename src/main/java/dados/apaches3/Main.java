package dados.apaches3;

import conexao.banco.*;
import log.datas.GerarLog;
import log.datas.S3Logs;
import log.datas.SlackNotificador;
import log.datas.SlackNotificador;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Main {

    // se estiver true irá baixar todos os arquivos do bucket
    static Boolean baixarConteudo = true;
    static Conexao conectarBd;

    static {
        try {
            conectarBd = new Conexao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        
        SlackNotificador.envioNotificacao("Iniciando Atualização da base");

        S3Client s3Client = new S3Provider().getS3Client();
        S3Logs.listarBuckets(s3Client);
        ListObjectsRequest listObj = S3Logs.listarObjetosBucket(s3Client);
        if(baixarConteudo){

            SlackNotificador.envioNotificacao("Baixando Contéudo do S3...");
            S3Logs.baixarObjetosBucket(s3Client, listObj);
        }

        SlackNotificador.envioNotificacao("Contéudo do S3 baixado...");
        System.out.println("Iniciando Leitura...");
        List<List<Object>> planilha = LeitorExcel.extrairDadosPlanilha("./arquivos/SPDadosCriminais_2024.xlsx");

        ManipularDados manipular = new ManipularDados();

        SlackNotificador.envioNotificacao("Iniciando Atualização - 0%");
        manipular.extrairBairros(planilha);

        SlackNotificador.envioNotificacao("Atualização em Andamento - 25%");
        manipular.extrairLocais(planilha);

        SlackNotificador.envioNotificacao("Atualização em Andamento - 50%");
        manipular.extrairLogradouro(planilha);

        SlackNotificador.envioNotificacao("Atualização em Andamento - 75%");
        manipular.extrairCrimes(planilha);

        SlackNotificador.envioNotificacao("Atualização Finalizada - 100%");
    }
}
