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

            // 11 -> coluna onde possui os valores das celulas dos bairros
            String bairro = Objects.isNull(planilha.get(item).get(11)) ? "" : planilha.get(item).get(11).toString();
            Boolean jaExtraido = Bairros.stream().anyMatch(x -> x.contains(bairro));
            if(!jaExtraido && bairro.length() > 0){
                Bairros.add(bairro);
            }
        }
        bancoDados.cadastrarBairrosBd(Bairros);
    }
}
