package dados.apaches3;


import org.apache.poi.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        IOUtils.setByteArrayMaxOverride(527836947);
        String nomeArquivo = "SPDadosCriminais_2024.xlsx";

        // Carregando o arquivo excel
        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo
        LeitorExcel leitorExcel = new LeitorExcel();
        List<Bairro> bairrosExtraidos = leitorExcel.extrarBairros(nomeArquivo, arquivo);

        // Fechando o arquivo após a extração
        arquivo.close();

        System.out.println("Bairros extraídos:");
        for (Bairro bairro : bairrosExtraidos) {
            System.out.println(bairro);
        }
    }
}
