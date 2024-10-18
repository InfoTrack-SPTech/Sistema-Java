package conexao.banco;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManipularDados {

    static RegistrarDados bancoDados = new RegistrarDados();
    public static void extrairBairros(List<List<Object>> planilha){

        List<String> Bairros = new ArrayList<>();
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 12 -> coluna onde possui os valores das celulas dos bairros
            String bairro = Objects.isNull(planilha.get(item).get(12)) ? "" : planilha.get(item).get(12).toString();
            Boolean jaExtraido = Bairros.stream().anyMatch(x -> x.contains(bairro));
            if(!jaExtraido && bairro.length() > 0){
                Bairros.add(bairro);
            }
        }
        bancoDados.cadastrarBairrosBd(Bairros);
    }

    public static void extrairLogradouros(List<List<Object>> planilha){

        List<String> Nomes = new ArrayList<>();
        List<String> Numeros = new ArrayList<>();
        List<String> Latitudes = new ArrayList<>();
        List<String> Longitudes = new ArrayList<>();
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 13 -> coluna onde possui os valores das celulas dos logradouros
            String nome = Objects.isNull(planilha.get(item).get(13)) ? "" : planilha.get(item).get(13).toString();

            // 14 -> coluna onde possui os valores das celulas dos numeros
            String numero = Objects.isNull(planilha.get(item).get(14)) ? "" : planilha.get(item).get(14).toString();

            // 15 -> coluna onde possui os valores das celulas das latitudes
            String latitude = Objects.isNull(planilha.get(item).get(15)) ? "" : planilha.get(item).get(15).toString();

            // 16 -> coluna onde possui os valores das celulas das longitudes
            String longitude = Objects.isNull(planilha.get(item).get(16)) ? "" : planilha.get(item).get(16).toString();

            Boolean jaExtraido = Nomes.stream().anyMatch(x -> x.contains(nome)) && Numeros.stream().anyMatch(x -> x.contains(numero)) && Latitudes.stream().anyMatch(x -> x.contains(latitude)) && Longitudes.stream().anyMatch(x -> x.contains(longitude));
            if(!jaExtraido && nome.length() > 0 && numero.length() > 0 && latitude.length() > 0 && longitude.length() > 0){
                Nomes.add(nome);
                Numeros.add(numero);
                Latitudes.add(latitude);
                Longitudes.add(longitude);
            }
        }
        bancoDados.cadastrarLogradourosBd(Nomes,Numeros,Latitudes,Longitudes);
    }

    public static void extrairCrimes(List<List<Object>> planilha){

        List<String> Crimes = new ArrayList<>();
        List<String> Datas = new ArrayList<>();
        List<String> Descricoes = new ArrayList<>();
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 23 -> coluna onde possui os valores das celulas dos crimes
            String crime = Objects.isNull(planilha.get(item).get(23)) ? "" : planilha.get(item).get(23).toString();

            // 8 -> coluna onde possui os valores das celulas das datas
            String dataOcorrencia = Objects.isNull(planilha.get(item).get(8)) ? "" : planilha.get(item).get(8).toString();

            // 21 -> coluna onde possui os valores das celulas das rubricas
            String descricao = Objects.isNull(planilha.get(item).get(21)) ? "" : planilha.get(item).get(21).toString();

            Boolean jaExtraido = Crimes.stream().anyMatch(x -> x.contains(crime)) && Datas.stream().anyMatch(x -> x.contains(dataOcorrencia)) && Descricoes.stream().anyMatch(x -> x.contains(descricao));
            if(!jaExtraido && crime.length() > 0 && dataOcorrencia.length() > 0 && descricao.length() > 0){
                Crimes.add(crime);
                Datas.add(dataOcorrencia);
                Descricoes.add(descricao);
            }
        }
        bancoDados.cadastrarCrimesBd(Crimes,Datas,Descricoes);
    }

    public static void extrairLocais(List<List<Object>> planilha){

        List<String> Locais = new ArrayList<>();
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 11 -> coluna onde possui os valores das celulas dos locais
            String local = Objects.isNull(planilha.get(item).get(11)) ? "" : planilha.get(item).get(11).toString();
            Boolean jaExtraido = Locais.stream().anyMatch(x -> x.contains(local));
            if(!jaExtraido && local.length() > 0){
                Locais.add(local);
            }
        }
        bancoDados.cadastrarLocaisBd(Locais);
    }
}
