package conexao.banco;

import log.datas.GerarLog;

import java.io.IOException;
import java.util.*;

import static ManipularDados.Bd;

public class ManipularDados {

    static RegistrarDados Bd = new RegistrarDados();

    public ManipularDados() throws IOException {
    }

    public static void extrairBairros(List<List<Object>> planilha) throws IOException {

        new GerarLog("extrairBairros", "Iniciando extração das informações");

        List<String> Bairros = new ArrayList<>();
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 11 -> coluna onde possui os valores das celulas dos bairros
            String bairro = Objects.isNull(planilha.get(item).get(11)) ? "" : planilha.get(item).get(11).toString();
            Boolean jaExtraido = Bairros.stream().anyMatch(x -> x.equalsIgnoreCase(bairro));
            if(!jaExtraido && bairro.length() > 0){
                Bairros.add(bairro);
            }
        }
        new GerarLog("extrairBairros", "Finalizando extração das informações");
        Bd.cadastrarBairrosBd(Bairros);
    }
    public static void extrairLogradouro(List<List<Object>> planilha) throws IOException {

        new GerarLog("extrairLogradouro", "Iniciando extração das informações");

        List<Bairro> bairros = Bd.consultarBairros();
        List<Logradouro> ruasCadastradas = new ArrayList<>();
        for(int i = 1; i <= planilha.size() - 1; i++){

            // "????????????" -> Caso o logradouro não possua endereço será associado está informação que está registrada no banco
            String nomeBairro = Objects.isNull(planilha.get(i).get(11)) ? "????????????" : planilha.get(i).get(11).toString();
            Integer idBairro = bairros.stream().filter(x -> x.getNome().equalsIgnoreCase(nomeBairro))
                                                  .map(Bairro::getIdBairro)
                                                  .findFirst()
                                                  .orElse(0);

            // 12 -> coluna com o valor do endereço
            String endereco = Objects.isNull(planilha.get(i).get(12)) ? "Não Informado" : planilha.get(i).get(12).toString();

            // 13 -> coluna com o valor do numero do endereço
            String numero = Objects.isNull(planilha.get(i).get(13)) ? "0" : planilha.get(i).get(13).toString();

            // 14 -> coluna com o valor latitude
            String vlLatitude = planilha.get(i).get(14).toString().replace(",", ".");
            Double latitude = Objects.isNull(planilha.get(i).get(14)) || vlLatitude.equalsIgnoreCase("NULL") ? 0.0 : Double.parseDouble(vlLatitude);

            // 15 -> coluna com o valor longitude
            String vlLongitude = planilha.get(i).get(15).toString().replace(",", ".");
            Double longitude = Objects.isNull(planilha.get(i).get(15)) || vlLongitude.equalsIgnoreCase("NULL") ? 0.0 : Double.parseDouble(vlLongitude);

            // Verifica se a rua com mesmo nome e numero já esta adicionada, se sim, irá pular essa inserção no banco de dados
            Boolean temRua = ruasCadastradas.stream().anyMatch(x -> x.getNome().equalsIgnoreCase(endereco) && x.getNumero().equals(numero));
            if(!temRua){
                ruasCadastradas.add(new Logradouro(0, endereco, numero, 0));
                Bd.cadastrarLogradourosBd(new Logradouro(endereco, numero, idBairro, latitude, longitude));
            }

            System.out.println("Quantidade lida: " + i);
        }

        new GerarLog("ExtrairLogradouro", "Finalizando extração das informações");
    }

    public static void extrairLocais(List<List<Object>> planilha) throws IOException {

        new GerarLog("extrairLocais", "Iniciando extração das informações");
        List<String> Locais = new ArrayList<>();
        // Define os tipos de estabelecimentos permitidos
        Set<String> tiposPermitidos = new HashSet<>(Arrays.asList(
                "Casa", "Apartamento","Casas","Apartamentos", "Moradia", "Edícula/Fundos", "Condomínio Residencial",
                "Lojas", "Restaurante", "Bar/Botequim", "Mercado", "Escritórios",
                "Agência Bancária", "Salão de Beleza/Estética", "Café/Lanchonete",
                "Padaria/Confeitaria", "Farmácia/Drogaria", "Autopeças", "Pet Shop",
                "Shopping Center"
        ));
        for (int item = 1; item <= planilha.size() - 1; item++){

            // 10 -> coluna onde possui os valores das celulas dos locais
            String local = Objects.isNull(planilha.get(item).get(10)) || !tiposPermitidos.contains(planilha.get(item).get(10)) ? "" : planilha.get(item).get(10).toString();

            // Verifica se o tipo de local está na lista de permitidos

            Boolean jaExtraido = Locais.stream().anyMatch(x -> x.equalsIgnoreCase(local));
            if(!jaExtraido && local.length() > 0){
                Locais.add(local);
            }
        }
        new GerarLog("extrairLocais", "Finalizando extração das informações");
        Bd.cadastrarLocaisBd(Locais);
    }
}

public static void extrairCrimes(List<List<Object>> planilha) throws IOException {

    new GerarLog("extrairCrimes", "Iniciando extração das informações");

    List<Local> locais =
    List<Logradouro> logradouros = Bd.consultarLogradouros();
    List<Crime> crimesCadastrados = new ArrayList<>();
    for(int i = 1; i <= planilha.size() - 1; i++){

        // "Não informado" -> Caso o crime não possua logradouro será associado está informação que está registrada no banco
        String nomeLogradouro = Objects.isNull(planilha.get(i).get(12)) ? "Não informado" : planilha.get(i).get(12).toString();
        Integer idLogradouro = logradouros.stream().filter(x -> x.getNome().equalsIgnoreCase(nomeLogradouro))
                .map(Logradouro::getIdLogradouro)
                .findFirst()
                .orElse(0);

        // "Não informado" -> Caso o crime não possua local será associado está informação que está registrada no banco
        String nomeLocal = Objects.isNull(planilha.get(i).get(10)) ? "Não informado" : planilha.get(i).get(10).toString();
        Integer idLocal = logradouros.stream().filter(x -> x.getNome().equalsIgnoreCase(nomeLocal))
                .map(Local::getIdLocal)
                .findFirst()
                .orElse(0);

        // 22 -> coluna com o valor da natureza do crime
        String natureza = Objects.isNull(planilha.get(i).get(22)) ? "Não Informado" : planilha.get(i).get(22).toString();

        // 20 -> coluna com o valor da descrição do crime
        String descricao = Objects.isNull(planilha.get(i).get(20)) ? "Não Informado" : planilha.get(i).get(20).toString();

        System.out.println("Quantidade lida: " + i);
    }

    new GerarLog("ExtrairCrime", "Finalizando extração das informações");
}