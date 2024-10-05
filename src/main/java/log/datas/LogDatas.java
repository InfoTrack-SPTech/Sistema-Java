package log.datas;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogDatas {
    public static void main(String[] args) {

        // tarefa que será executada em 1 thread, quanto maior o parametro, maior o numero de tarefas sendo executadas simultaneamente
        // Ela é criada para definirmos o intervalo entre os horarios
        ScheduledExecutorService agendar = Executors.newScheduledThreadPool(1);

        // esta tarefa irá executar uma funcao em segundo plano sem atrapalhar o fluxo da funcao main
        Runnable correr = new Runnable() {
            int cont = 0;
            @Override

            // este metodo irá rodar quantas vezes for necessario, neste caso serao 3,
            // o IF não e necessário, só evita de ficar num loop
            public void run() {
                if(cont < 3){

                    List<String> diasSemana = List.of("Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado");
                    List<String> mesesAno = List.of("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");

                    Date dtAtual = new Date();
                    String horario = """
                            %d:%d:%d""".formatted(dtAtual.getHours(), dtAtual.getMinutes(), dtAtual.getSeconds());

                    String msgFinal = String.format("%s %s do dia %d de %s de %d", diasSemana.get(dtAtual.getDay()), horario, dtAtual.getDate(), mesesAno.get(dtAtual.getMonth()), LocalDate.now().getYear());
                    System.out.println(msgFinal);

                    cont++;
                } else{
                    /* para fazer o 'correr' para de rodar, é necessário desligar o agendar
                     que é a thread responsavel por sua execucao */
                    agendar.shutdown();
                }
            }
        };

         /* aqui definimos os intervalos entre os logs, 1° funcao que executaremos, 2° delay para executar dps que
         a outra tarefa foi executada(delay de zero), 3° qual será o tempo entre os intervalos e 4° a medida
         de tempo passada na 3°, pode ser em segundos, minutos etc. */
        agendar.scheduleAtFixedRate(correr, 0, 5, TimeUnit.SECONDS);
    }
}
