package dados.apaches3;

public class Crime {
    private Integer idCrime;
    private String natureza;
    private String dataOcorrencia;
    private String descricao;
    private Integer fkLogradouro;
    private Integer fkLocal;

    public Crime() {}

    public Crime(Integer idCrime, String natureza, String dataOcorrencia, String descricao, Integer fkLogradouro, Integer fkLocal) {
        this.idCrime = idCrime;
        this.natureza = natureza;
        this.dataOcorrencia = dataOcorrencia;
        this.descricao = descricao;
        this.fkLogradouro = fkLogradouro;
        this.fkLocal = fkLocal;
    }

    public Integer getIdCrime() {
        return idCrime;
    }

    public void setIdCrime(Integer idCrime) {
        this.idCrime = idCrime;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(String dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getFkLogradouro() {
        return fkLogradouro;
    }

    public void setFkLogradouro(Integer fkLogradouro) {
        this.fkLogradouro = fkLogradouro;
    }

    public Integer getFkLocal() {
        return fkLocal;
    }

    public void setFkLocal(Integer fkLocal) {
        this.fkLocal = fkLocal;
    }

    @Override
    public String toString() {
        return "\nCrime {" +
                "idCrime = " + idCrime +
                ", natureza = '" + natureza + '\'' +
                ", dataOcorrencia = '" + dataOcorrencia + '\'' +
                ", descricao = '" + descricao + '\'' +
                ", fkLogradouro = " + fkLogradouro +
                ", fkLocal = " + fkLocal +
                '}';
    }
}

