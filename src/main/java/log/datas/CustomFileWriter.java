package log.datas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomFileWriter extends FileWriter {

    // Construtor da classe, que usa o construtor de FileWriter
    private CustomFileWriter(File file, boolean append) throws IOException {
        super(file, append);
    }

    // Método para inserir texto no arquivo
    private void insertText(String text) throws IOException {
        // Escreve o texto fornecido no arquivo
        write(text);
    }

    /*
         Seguir o padrão:
         nomeMetodo -> Nome da funcionalidade aonde o metodo "AdicionarRegistroLog" está sendo chamado.
         Log -> Texto a ser adicionado no log. A mensagem precisa estar de acordo com o nome função.
         EX: BuscarRegistrosFiltro(){
              AdicionarRegistroLog("BuscarRegistroFiltro", "buscando registros com filtro");
              return null;
         }
         Sempre executar "AdicionarRegistroLog" antes do return da função em que é chamado.
    */
    public static void AdicionarRegistroLog(String nomeMetodo, String Log){
        try {
            // Caminho do arquivo de log
            File logFile = new File("""
                    ./src/main/java/log/datas/logs/%s.txt""".formatted(nomeMetodo));

            // Cria uma instância de CustomFileWriter, com append = true (adiciona no final do arquivo)
            CustomFileWriter customWriter = new CustomFileWriter(logFile, true);

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime horario = LocalDateTime.now();

            // inseri no arquivo o texto abaixo
            customWriter.insertText(horario.format(formatador) + " - " + Log + " \n");

            // Sempre fechar o writer ao finalizar
            customWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
