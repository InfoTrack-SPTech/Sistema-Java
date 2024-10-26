package dados.apaches3;

import conexao.banco.Bairro;
import conexao.banco.ManipularDados;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Main {

    // se estiver true irá baixar todos os arquivos do bucket
    static Boolean baixarConteudo = false;

    public static void main(String[] args) throws Exception {

//        S3Client s3Client = new S3Provider().getS3Client();
//        listarBuckets(s3Client);
//        ListObjectsRequest listObj = listarObjetosBucket(s3Client);
//        if(baixarConteudo){
//            baixarObjetosBucket(s3Client, listObj);
//        }

        List<List<Object>> planilha = LeitorExcel.extrairDadosPlanilha("./src/main/java/dados/apaches3/arquivos/SPDadosCriminais_2024.xlsx");

        ManipularDados manipular = new ManipularDados();
        manipular.extrairBairros(planilha);
        manipular.extrairLogradouro(planilha);
        manipular.extrairCrimes(planilha);
        manipular.extrairLocais(planilha);

    }

    public static void listarBuckets(S3Client s3Client){
        List<Bucket> buckets = s3Client.listBuckets().buckets();
        System.out.println("Lista de buckets:");
        for (Bucket bucket : buckets) {
            System.out.println("- " + bucket.name());
        }
    }

    public static ListObjectsRequest listarObjetosBucket(S3Client s3Client){
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket("s3-infotrack")
                .build();

        List<S3Object> objects = s3Client.listObjects(listObjects).contents();
        for (S3Object object : objects) {
            System.out.println("Objeto: " + object.key());
        }

        return listObjects;
    }

    public static void baixarObjetosBucket(S3Client s3Client, ListObjectsRequest listObjects) throws IOException {

        List<S3Object> objects = s3Client.listObjects(listObjects).contents();
        String caminho = "./src/main/java/dados/apaches3/arquivos";

        if(baixarConteudo){
            List<S3Object> arquivoDadosCriminais = s3Client.listObjects(listObjects).contents();
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket("s3-infotrack")
                        .key(object.key())
                        .build();

                InputStream objectContent = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                Files.copy(objectContent, Paths.get( caminho, object.key()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
