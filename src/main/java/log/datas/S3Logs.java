package log.datas;

import dados.apaches3.S3Provider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class S3Logs {

    static S3Client s3Client = new S3Provider().getS3Client();

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
        String caminho = "./arquivos";

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

    // Esta função somente deve ser utilizada ao finalizar a geração de log no java, pois ele vai pegar a
    // versão do log local para substituir a versão que está no bucket S3
    public static void subirArquivoBucket(String nomeArquivo){

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("s3-logs-infotrack")
                .key(nomeArquivo)
                .contentType("text/plain")
                .build();

        File file = new File("""
                ./logs/%s.txt""".formatted(nomeArquivo));
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }
}
