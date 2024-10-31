package utils;

import conexao.banco.Bairro;
import conexao.banco.Local;
import conexao.banco.Logradouro;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ManipularDadosUtils {

    public String validarValorTexto(Object valor){

        String informacao = Objects.isNull(valor) || valor.toString().equalsIgnoreCase("NULL")? "Não Informado" : valor.toString();
        return informacao;
    }

    public String validarValorNumerico(Object valor){

        String informacao = Objects.isNull(valor) || valor.toString().equalsIgnoreCase("NULL")? "0" : valor.toString().replace(",", ".");
        return informacao;
    }

    public String tranformarPadraoDataAnoMesDia(Object valorData, Object valorHorario) throws ParseException {

        SimpleDateFormat formatoEntradaData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoEntradaHora = new SimpleDateFormat("HH:mm:ss");

        String registroData = Objects.isNull(valorData) ? "" : valorData.toString();
        String registroHora = Objects.isNull(valorHorario) || valorHorario.toString().equalsIgnoreCase("NULL")? "00:00:00" : valorHorario.toString();

        Calendar calendarioData = Calendar.getInstance();
        calendarioData.setTime(formatoEntradaData.parse(registroData));
        Integer dia = calendarioData.get(Calendar.DAY_OF_MONTH);
        Integer mes = calendarioData.get(Calendar.MONTH) + 1; // Janeiro começa no 0, por isso é adicionado 1
        Integer ano = calendarioData.get(Calendar.YEAR);
        String modeloDataDesejado = """
                %s-%s-%s""".formatted(ano, mes, dia);

        Calendar calendarioHora = Calendar.getInstance();
        calendarioHora.setTime(formatoEntradaHora.parse(registroHora));
        Integer horas = calendarioHora.get(Calendar.HOUR_OF_DAY);
        Integer minutos = calendarioHora.get(Calendar.MINUTE);
        Integer segundos = calendarioHora.get(Calendar.SECOND);
        String modeloHoraDesejado = """
                %s:%s:%s""".formatted(horas, minutos, segundos);

        return modeloDataDesejado + " " + modeloHoraDesejado;
    }

    public Integer validarConsultaLogradouroPorEnderecoENumero(List<Logradouro> logradouros, String end, String num){

        Integer res = logradouros.stream()
                                    .filter(x -> x.getNome().equalsIgnoreCase(end))
                                    .map(Logradouro::getIdLogradouro)
                                    .findFirst().orElse(null);

        return res == null ? 0 : res;
    }

    public Integer validarConsultaLocalPorNome(List<Local> locais, String local){

        Integer idLocal = locais.stream()
                .filter(x -> x.getNome().equalsIgnoreCase(local))
                .map(Local::getIdLocal)
                .findFirst()
                .orElse(0);
        return idLocal;
    }

    public Integer validarConsultaBairroPorNome(List<Bairro> bairros, String nmBairro){

        Integer idBairro = bairros.stream().filter(x -> x.getNome().equalsIgnoreCase(nmBairro))
                .map(Bairro::getIdBairro)
                .findFirst()
                .orElse(0);
        return idBairro;
    }
}
