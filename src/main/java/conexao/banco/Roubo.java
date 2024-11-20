package conexao.banco;

public class Roubo extends Crime{

    private String natureza;
    private String artigo;

    public Roubo(Integer idCrime, String dataOcorrencia, Integer fkLogradouro, Integer fkLocal, String artigo) {
        super(idCrime, dataOcorrencia, fkLogradouro, fkLocal);
        this.natureza = "ROUBO";
        this.artigo = artigo;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getArtigo() {
        return artigo;
    }

    public void setArtigo(String artigo) {
        this.artigo = artigo;
    }
}
