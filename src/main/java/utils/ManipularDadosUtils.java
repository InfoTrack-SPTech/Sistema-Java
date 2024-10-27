package utils;

import conexao.banco.Bairro;
import conexao.banco.Local;
import conexao.banco.Logradouro;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        SimpleDateFormat formatoSaidaData = new SimpleDateFormat("yyyy-MM-dd"); // Formato desejado

        SimpleDateFormat formatoEntradaHora = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatoSaidaHora = new SimpleDateFormat("HH-mm-ss"); // Formato desejado

        String registroData = Objects.isNull(valorData) ? "" : valorData.toString();
        String registroHora = Objects.isNull(valorHorario) || valorHorario.toString().equalsIgnoreCase("NULL")? "00:00:00" : valorHorario.toString();

        Date data = formatoEntradaData.parse(registroData);
        Date hora = formatoEntradaHora.parse(registroHora);

        return formatoSaidaData.format(data) + " " + formatoSaidaHora.format(hora);
    }

    public Integer validarConsultaLogradouroPorEnderecoENumero(List<Logradouro> logradouros, String end, String num){

        System.out.println(logradouros.get(2));
        System.out.println(end);
        System.out.println(num);

        Integer res = logradouros.stream()
                                    .filter(x -> x.getNome().equalsIgnoreCase(end))
                                    .map(Logradouro::getIdLogradouro)
                                    .findFirst().orElse(null);

        System.out.println("idadsf");
        System.out.println(res);

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
