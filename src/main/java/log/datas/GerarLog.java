package log.datas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GerarLog extends FileWriter {

    /*
         Seguir o padrão:
         nomeMetodo -> Nome da funcionalidade aonde o metodo "AdicionarRegistroLog" está sendo chamado.
         Log -> Texto a ser adicionado no log. A mensagem precisa estar de acordo com o nome função.
         EX: BuscarRegistrosFiltro(){
              AdicionarRegistroLog("BuscarRegistroFiltro", "buscando registros com filtro");
              return null;
         }
         Sempre executar "AdicionarRegistroLog" antes da função iniciar e antes de finalizar
    */
    public GerarLog(String nomeMetodo, String Log) throws IOException {
        super(nomeMetodo);
        try {
            // Caminho do arquivo de log
            File logFile = new File("""
                    ./logs/%s.txt""".formatted(nomeMetodo));

            // Cria uma instância de arquivoLog, com append = true (adiciona no final do arquivo)
            GerarLog arquivoLog = new GerarLog(logFile, true);

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime horario = LocalDateTime.now();

            // inseri no arquivo o texto abaixo
            arquivoLog.insertText(horario.format(formatador) + " - " + Log + " \n");

            // Sempre fechar o writer ao finalizar
            arquivoLog.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Construtor da classe, que usa o construtor de FileWriter
    public GerarLog(File file, boolean append) throws IOException {
        super(file, append);
    }

    // Método para inserir texto no arquivo
    private void insertText(String text) throws IOException {
        // Escreve o texto fornecido no arquivo
        write(text);
    }
}
